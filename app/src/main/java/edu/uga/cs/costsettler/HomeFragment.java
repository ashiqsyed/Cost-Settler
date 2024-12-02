package edu.uga.cs.costsettler;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String user = NavigationHostActivity.getUser();
        if (user != null) {
            user = user.substring(0, user.indexOf("@"));
        }
        String overview = "Welcome " + user + " to the Cost Settler App: a place for roommates to " +
                "track costs for communal goods.\n\nStart by adding an item from the Add Item option in " +
                "the navigation menu in the top left.\n\nWhen you want to settle costs go to the Cost " +
                "Overview option in the menu to look over expenses and settle purchases.";
        TextView textView = view.findViewById(R.id.overviewText);
        textView.setText(overview);
    }
}