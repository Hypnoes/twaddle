
Meaning of symbols:

N :=> Negitive
B :=> Band
P :=> Positive

tab1. 8 kinds of oil field's cost and profit

O = { o1 ... o8 } := 8 kinds oil-field

theta_*P          := profits of mining while the mine do have oil
phi_*P            := costs of mining while the mine do have oil

theta_*N          := profits of mining while the mine is empty
phi_*P            := cost of mining while the mine is empty

theta_P* | phi_P* := profits or costs of action "do"
theta_B* | phi_B* := profits or costs of action "decide later"
theta_N* | phi_N* := profits or costs of action "ignore"

part-00000-1fb532bc-f82a-403e-b1bf-1ba76074cba9-c000. threshold parameters of 8 kinds of oil field (as we choose Pr=0.35)

Decision made by following rules:

    alpha = ((theta_BN - theta_PN) + (phi_PN - phi_BN)) / 
        ((theta_BN - theta_PN) + (phi_PN - phi_BN) + (theta_PP - theta_BP) + (phi_BP - phi_PP))
    beta  = ((theta_NN - theta_BN) + (phi_BN - phi_NN)) /
        ((theta_NN - theta_BN) + (phi_BN - phi_NN) + (theta_BN - theta_NP) + (phi_NP - phi_BN))
    gamma = ((theta_NN - theta_PN) + (phi_PN - phi_NN)) /
        ((theta_NN - theta_PN) + (phi_PN - phi_NN) + (theta_PP - theta_NP) + (phi_NP - phi_PP))
    decs(pr: Double) = pr match {
        case _ if(pr >= alpha)                => "POS"
        case _ if(alpha > pr && pr >= beta)   => "BND"
        case _ if(beta > pr)                  => "NEG"
    }
