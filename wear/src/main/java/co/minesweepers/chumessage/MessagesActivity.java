package co.minesweepers.chumessage;

import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.support.wearable.view.WearableListView;

import co.minesweepers.chumessage.adapters.Adapter;

public class MessagesActivity extends WearableActivity implements WearableListView.ClickListener{

    // Sample dataset for the list
    String[] elements = { "List Item 1", "List Item 2", "List Item 2" , "List Item 2" ,"List Item 2"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        // Get the list component from the layout of the activity
        WearableListView listView =
        (WearableListView) findViewById(R.id.wearable_list);

        // Assign an adapter to the list
        listView.setAdapter(new Adapter(this, elements));

        // Set a click listener
        listView.setClickListener(this);
     }

    // WearableListView click listener
    @Override
    public void onClick(WearableListView.ViewHolder v) {
        Integer tag = (Integer) v.itemView.getTag();
        // use this data to complete some action ...
        }

    @Override
    public void onTopEmptyRegionClick() {

    }
}
