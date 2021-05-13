package kr.pnu.project10.Fragments.ViewModels;

import androidx.lifecycle.ViewModel;

public class SignupViewModel extends ViewModel {
    private static final int RC_SIGN_IN = 9001;

    public static int getRcSignIn() {
        return RC_SIGN_IN;
    }
}