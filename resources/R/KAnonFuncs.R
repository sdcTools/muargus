# Author P.P. de Wolf
# Version 26 February 2018
# Thanks to Bernhard Meindl for first versions of mu_to_sdc_importance and run_Kanon

# returns sdcImportance: 1 is the variable that should be least likely
# to have suppressions - the one with highest value in the mu importance vector
mu_to_sdc_importance <- function(keyVars, mu_imp) {
  o <- order(mu_imp, decreasing = TRUE)
  match(keyVars, keyVars[o])
}

# converts all variables to strings and replaces NA by missing-strings
replace_NA_per_var <- function(dat, params){
  for (i in seq_along(params)){
    kV <- params[[i]]$keyVars
    missings <- params[[i]]$missings
    dat[kV] <- data.frame(lapply(dat[kV], as.character), stringsAsFactors = FALSE)
  
    for (j in 1:length(kV)){
      dat[is.na(dat[kV[j]]),kV[j]] <- missings[[j]][1]
    }
  } 
  return(dat)
}

# Get indexes of records with missing code, per code (max 2 missing codes per variable)
get_missings <- function(dat,params){
  result <- list()
  k <- 1
  for (i in seq_along(params)){ # loop over set of tables/combinations
    kV <- params[[i]]$keyVars
    missings <- params[[i]]$missings
    for (j in 1:length(kV)){ # loop over variables per table/combination
      result[[k]] <- list()
      for (m in 1:length(missings[[j]]))
        result[[k]][[m]] <- which(dat[,kV[j]] == missings[[j]][m])
      k <- k+1
    }
  }
  return(result)
}

# Replaces all missing-codes (max 2 missing codes per variable) with <NA> 
# to be used by sdcMicro#kAnon()
missings_to_NA <- function(dat,params,missing_indices){
  k <- 1
  for (i in seq_along(params)){ # loop over tables
    kV <- params[[i]]$keyVars
    for (j in 1:length(kV)){ # loop over variables per table
      for (m in 1:length(missing_indices[[k]])){
        dat[missing_indices[[k]][[m]],kV[j]] <- NA
      }
      k <- k+1
    }
  }
  return(dat)
}

# Sets back missings that were present before applying kAnon()
NA_to_missings <- function(dat,params,missing_indices){
  k <- 1
  for (i in seq_along(params)){ # loop over tables
    kV <- params[[i]]$keyVars
    missings <- params[[i]]$missings
    for (j in 1:length(kV)){ # loop over variables per table
      for (m in 1:length(missing_indices[[k]])){
        if (length(missing_indices[[k]][[m]]) != 0){
          dat[missing_indices[[k]][[m]],kV[j]] <- missings[[j]][m]
        }
      }
      k <- k+1
    }
  }
  return(dat)
}

# run k-anonymisation as many times as there are combinations listed in params-list
# params[[i]]$keyVars: vector with numbers of columns of key-variables in dataframe
# params[[i]]$k: threshold as specified in mu-argus, i.e. (k+1)-anonymity
# params[[i]]$priorities_mu: vector with priorities as specified in mu-argus
#                            or NULL in case no priorities are to be used
# params[[i]]$missings: vector with codes for NA in same order as keyVars
# returns updated dataframe and table of added suppressions
run_Kanon <- function(dat, params) {
  # First determine where the "original" missings are located per variable
  missing_indices <- get_missings(dat,params)
  # Then replace original missings by <NA>
  dat <- missings_to_NA(dat,params,missing_indices)
  # Apply kAnon()
  suppressions <- list()
  for (i in seq_along(params)) {
    k <- params[[i]]$k
    
    kV <- params[[i]]$keyVars
    
    if (!is.null(params[[i]]$priorities_mu)){
      imp_sdc <- mu_to_sdc_importance(keyVars=kV, mu_imp=params[[i]]$priorities_mu)
    }
    else imp_sdc <- NULL
    
    # run (k+1)-anonymisation and update dataframe
    res <- kAnon(dat, keyVars=kV, importance=imp_sdc, k=k+1)
    dat[,kV] <- res$xAnon
    suppressions[[i]] <- res$supps
  }
  # Set back original missings with their missing code
  dat <- NA_to_missings(dat,params,missing_indices)
  # Replace additional <NA> with first missing code
  dat <- replace_NA_per_var(dat,params)
  
  return(list(dat=dat,supps=suppressions))
}