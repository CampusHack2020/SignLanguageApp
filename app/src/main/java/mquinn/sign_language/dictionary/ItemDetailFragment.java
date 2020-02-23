package mquinn.sign_language.dictionary;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import mquinn.sign_language.R;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private Content.Word mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = Content.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.word);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.item_detail)).setText(mItem.details);

//            Leo's special moment -> mItem.word.split("(?!^)")

            System.out.println(mItem.word);

            char[] chars = mItem.word.toCharArray();

            for(int i = 0; i < chars.length; i++) {
                int resourceId = getResources().getIdentifier("a", "drawable", "mquinn.sign_language.dictionary");//getResources().getIdentifier(String.valueOf(Character.toLowerCase(chars[i])), "drawable", "mquinn.sign_language.dictionary");
                System.out.println(resourceId + ", " + String.valueOf(Character.toLowerCase(chars[i])) + ", " + this.getClass().getPackage().toString());
                ImageView image = new ImageView(rootView.getContext());

                Bitmap bMap = BitmapFactory.decodeResource(getResources(), resourceId);

                Bitmap bMapScaled = null;

                if (bMap != null)
                    bMapScaled = Bitmap.createScaledBitmap(bMap, bMap.getWidth() / 8, bMap.getHeight() / 8, true);

                image.setImageBitmap(bMapScaled);
                rootView.addView(image);
            }

        }

        return rootView;
    }
}
