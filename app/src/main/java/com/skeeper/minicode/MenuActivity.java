package com.skeeper.minicode;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.skeeper.minicode.databinding.ActivityMenuBinding;
import com.skeeper.minicode.helpers.animations.BackInterpolations;

public class MenuActivity extends AppCompatActivity {


    private ActivityMenuBinding binding;

    private ImageButton activeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
//        getWindow().setNavigationBarColor(getResources().getColor(R.color.violet_average));
        getWindow().setNavigationBarColor(Color.TRANSPARENT);
        binding = ActivityMenuBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        // todo: migrate to nav
        binding.projectsButton.setOnClickListener(v -> {
            if (getCurrentFragment() instanceof ProjectsFragment) return;
            setFragment(new ProjectsFragment());
            switchActiveButton((ImageButton) v);
        });
        binding.settingsButton.setOnClickListener(v -> {
            if (getCurrentFragment() instanceof SettingsFragment) return;
            setFragment(new SettingsFragment());

            switchActiveButton((ImageButton) v);
        });
        binding.tutorialsButton.setOnClickListener(v -> {
            if (getCurrentFragment() instanceof TutorialsFragment) return;
            setFragment(new TutorialsFragment());
            switchActiveButton((ImageButton) v);
        });




        setFragment(new ProjectsFragment());
        setActiveButton(binding.projectsButton);
        //        binding.mainFragmentLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
//            @Override
//            public void onSwipeLeft() {
//                switchToNextFragment();
//            }
//
//            @Override
//            public void onSwipeRight() {
//                setFragment(new SettingsFragment());
//                switchActiveButton((ImageButton) binding.settingsButton);
//            }
//        });



    }

    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(binding.mainFragmentLayout.getId());

    }

    public void setFragment(Fragment newFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_up_fade_in, R.anim.slide_down_fade_out);
        fragmentTransaction.replace(R.id.mainFragmentLayout, newFragment);

//        fragmentTransaction.addToBackStack(null); // закомментил, чтобы не отлеживалась история фрагментов
        fragmentTransaction.commit();
    }






    private void setActiveButton(ImageButton button) {
        activeButton = button;
        button.setScaleX(1.0f);
        button.setScaleY(1.0f);
        button.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.violet_light)));
        button.setImageTintList(ColorStateList.valueOf(getColor(R.color.blue_light)));
    }
    private void switchActiveButton(ImageButton clickedButton) {
        if (activeButton == clickedButton) return;

        animateButton(activeButton, false); // Деактивация текущей кнопки
        animateButton(clickedButton, true); // Активация новой кнопки
        activeButton = clickedButton;
    }
    private void animateButton(ImageButton button, boolean activate) {
        float targetScale = activate ? 1.0f : 0.8f;
        int bgColor = activate ? getColor(R.color.violet_light) : getColor(R.color.violet);
        int tintColor = activate ? getColor(R.color.blue_light) : getColor(R.color.violet_light);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(button, "scaleX", targetScale);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(button, "scaleY", targetScale);

        var scaleInterpolator = new TimeInterpolator() {
            @Override
            public float getInterpolation(float input) {
                return BackInterpolations.backInOut(input);
            }
        };

        scaleX.setInterpolator(scaleInterpolator);
        scaleY.setInterpolator(scaleInterpolator);


        ValueAnimator bgAnimator = ValueAnimator.ofArgb(button.getBackgroundTintList().getDefaultColor(), bgColor);
        bgAnimator.addUpdateListener(anim ->
                button.setBackgroundTintList(ColorStateList.valueOf((int) anim.getAnimatedValue())));


        ValueAnimator tintAnimator = ValueAnimator.ofArgb(button.getImageTintList().getDefaultColor(), tintColor);
        tintAnimator.addUpdateListener(anim ->
                button.setImageTintList(ColorStateList.valueOf((int) anim.getAnimatedValue())));

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, bgAnimator, tintAnimator);
        animatorSet.setDuration(300);
        animatorSet.start();
    }

}