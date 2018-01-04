
Meaning of symbols:

N :=> Negitive
B :=> Band
P :=> Positive

Decision rules:

    alpha = (lambda_PN - lambda_BN) / 
        ((lambda_PN - lambda_BN) + (lambda_BP - lambda_PP))
    beta  = (lambda_BN - lambda_NN) /
        ((lambda_BN - lambda_NN) + (lambda_NP - lambda_BP))
    gamma = (lambda_PN - lambda_NN) /
        ((lambda_PN - lambda_NN) + (lambda_NP - lambda_PP))
    
    decs :
        if (pr >= alpha)                => "POS"
        if (alpha > pr && pr >= beta)   => "BND"
        if (beta > pr)                  => "NEG"

pr can be Bayes Probability or Logistic Regression Probability.

tab1. 8 kinds of oil field's cost and profit

O = { o1 ... o8 } := 8 kinds oil-field

theta_*P          := profits of mining while the mine do have oil
phi_*P            := costs of mining while the mine do have oil

theta_*N          := profits of mining while the mine is empty
phi_*N            := cost of mining while the mine is empty

theta_P* | phi_P* := profits or costs of action "do"
theta_B* | phi_B* := profits or costs of action "decide later"
theta_N* | phi_N* := profits or costs of action "ignore"

output. threshold parameters of 8 kinds of oil field (as we choose Pr=0.35)

tab2. 60 banks broke

SCALE:
a1 SALE

PROFIT:
a2 ROCE
a3 FFTL

OPERATION:
a4 GEAR
a5 CLTA

FLUIDITY:
a6 CACL
a7 QACL
a8 WCTA

OTHER:
a9 LAG
a10 AGE
a11 CHAUD
a12 BIG6

tab3. 4 companys lose function and threshold
