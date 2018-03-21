# Author P.P. de Wolf
# Version 26 February 2018
# Thanks to Bernhard Meindl for first versions of mu_to_sdc_importance and run_Kanon

# returns sdcImportance: 1 is the variable that should be least likely
# to have suppressions - the one with highest value in the mu importance vector
mu_to_sdc_importance <- function(keyVars, mu_imp) {
  o <- order(mu_imp, decreasing = TRUE)
  match(keyVars, keyVars[o])
}

# run k-anonymisation as many times as there are combinations listed in params-list
# params[[i]]$keyVars: vector with numbers of columns of key-variables in dataframe
# params[[i]]$k: threshold as specified in mu-argus, i.e. (k+1)-anonymity
# params[[i]]$priorities_mu: vector with priorities as specified in mu-argus
#                            or NULL in case no priorities are to be used
# params[[i]]$missings: vector with codes for NA in same order as keyVars
# returns updated dataframe and table of added suppressions
run_Kanon <- function(dat, params) {
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
  return(list(dat=dat,supps=suppressions))
}

# converts all variables to strings and replaces NA by missing-strings
replace_NA_per_var <- function(dat, params){
  for (i in seq_along(params)){
    kV <- params[[i]]$keyVars
    missings <- params[[i]]$missings
    dat[kV] <- data.frame(lapply(dat[kV], as.character), stringsAsFactors = FALSE)
  
    for (j in 1:length(kV)){
      dat[is.na(dat[kV[j]]),kV[j]] <- missings[j]
    }
  } 
  return(dat)
}
