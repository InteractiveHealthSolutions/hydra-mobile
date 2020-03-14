package com.ihsinformatics.dynamicformsgenerator.views.widgets.controls;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ihsinformatics.dynamicformsgenerator.R;
import com.ihsinformatics.dynamicformsgenerator.data.core.options.Option;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MultiSelectSpinner extends LinearLayout {
    public interface HandleSkipLogic {
        void applySkipLogic(int position) throws JSONException;
        void revertSkipLogic(int position) throws JSONException;
    }

    private Context context;
    private PopupWindow pw;
    private boolean expanded;        //to  store information whether the selected values are displayed completely or in shortened representatn
    public boolean[] checkSelected;    // store select/unselect information about the values in the list
    private MultiSelectSpinnerAdapter adapter;
    private List<Option> mItems;
    private TextView tv;
    private HandleSkipLogic handleSkipLogic;

    public MultiSelectSpinner(Context context, HandleSkipLogic handleSkipLogic, Option... items) {
        super(context);
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1));
        this.context = context;
        this.handleSkipLogic = handleSkipLogic;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.spinner_multiselect, this, false);
        addView(view);
        initialize(items);
    }

    public List<String> getSelectedValues() {
        List<String> toReturn = new ArrayList<String>();
        for (int i = 0; i < mItems.size(); i++) {
            if (checkSelected[i] == true) {
                toReturn.add(mItems.get(i).getText());
            }
        }
        return toReturn;
    }

    public List<Integer> getSelectedValuesPositions() {
        List<Integer> toReturn = new ArrayList<>();
        for (int i = 0; i < mItems.size(); i++) {
            if (checkSelected[i] == true) {
                toReturn.add(i);
            }
        }
        return toReturn;
    }

    public List<String> getValues() {
        List<String> toReturn = new ArrayList<String>();
        for (int i = 0; i < mItems.size(); i++) {
            toReturn.add(mItems.get(i).getText());
        }
        return toReturn;
    }

    public boolean isSelected(int position) {
        return checkSelected[position] == true;
    }

    /*
     * Function to set up initial settings: Creating the data source for drop-down list, initialising the checkselected[], set the drop-down list
     * */
    private void initialize(Option... items) {
        //data source for drop-down list
        mItems = Arrays.asList(items);
        checkSelected = new boolean[mItems.size()];
        //initialize all values of list to 'unselected' initially
        for (int i = 0; i < checkSelected.length; i++) {
            checkSelected[i] = false;
        }

	/*SelectBox is the TextView where the selected values will be displayed in the form of "Item 1 & 'n' more".
         * When this selectBox is clicked it will display all the selected values
    	 * and when clicked again it will display in shortened representation as before.
    	 * */
        tv = (TextView) findViewById(R.id.SelectBox);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                inflate();
            }
        });
        //onClickListener to initiate the dropDown list
        tv.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                initiatePopUp(mItems, tv);
            }
        });
    }

    public void inflate() {
        // TODO Auto-generated method stub
        if (!expanded) {
            //display all selected values
            String selected = "";
            int flag = 0;
            for (int i = 0; i < mItems.size(); i++) {
                if (checkSelected[i] == true) {
                    selected += mItems.get(i);
                    selected += ", ";
                    flag = 1;
                }
            }
            if (flag == 1)
                tv.setText(selected);
            expanded = true;
        } else {
            //display shortened representation of selected values
            tv.setText(adapter.getSelected() != null ? adapter.getSelected() : "");
            expanded = false;
        }
    }

    /*
     * Function to set up the pop-up window which acts as drop-down list
     * */
    private void initiatePopUp(List<Option> items, TextView tv) {
        LayoutInflater inflater = (LayoutInflater) MultiSelectSpinner.this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //get the pop-up window i.e.  drop-down layout
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.spinner_multi_select_list_popup, (ViewGroup) findViewById(R.id.PopUpView));
        //get the view to which drop-down layout is to be anchored
        LinearLayout layout1 = (LinearLayout) findViewById(R.id.relativeLayout1);
        pw = new PopupWindow(layout, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        //Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setTouchable(true);
        //let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
        pw.setOutsideTouchable(true);
        pw.setHeight(LayoutParams.WRAP_CONTENT);
        //dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
        pw.setTouchInterceptor(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            }
        });
        //provide the source layout for drop-down
        pw.setContentView(layout);
        //anchor the drop-down to bottom-left corner of 'layout1'
        pw.showAsDropDown(layout1);
        //populate the drop-down list
        final ListView list = (ListView) layout.findViewById(R.id.dropDownList);
        adapter = new MultiSelectSpinnerAdapter(context, items, tv, checkSelected);
        list.setAdapter(adapter);
    }

    private class MultiSelectSpinnerAdapter extends BaseAdapter {
        private ArrayList<Option> mListItems;
        private LayoutInflater mInflater;
        private TextView mSelectedItems;
        private int selectedCount = 0;
        private String firstSelected = "";
        private ViewHolder holder;
        private String selected = "";    //shortened selected values representation
        boolean[] checkSelected;

        public String getSelected() {
            return selected;
        }

        public void setSelected(String selected) {
            this.selected = selected;
        }

        public MultiSelectSpinnerAdapter(Context context, List<Option> items,
                                         TextView tv, boolean[] checkSelected) {
            mListItems = new ArrayList<Option>();
            mListItems.addAll(items);
            mInflater = LayoutInflater.from(context);
            mSelectedItems = tv;
            this.checkSelected = checkSelected;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return mListItems.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_item_multiselect_dropdown, null);
                holder = new ViewHolder();
                holder.chkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.chkbox.setText(mListItems.get(position).getText());
            final int position1 = position;
            // whenever the checkbox is clicked the selected values textview is updated with new selected values
            holder.chkbox.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    setText(position1);
                    CheckBox checkBox = (CheckBox)v;
                    if(checkBox.isChecked()){
                        try {
                            handleSkipLogic.applySkipLogic(position1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        try {
                            handleSkipLogic.revertSkipLogic(position1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            if (checkSelected[position])
                holder.chkbox.setChecked(true);
            else
                holder.chkbox.setChecked(false);
            return convertView;
        }

        /*
         * Function which updates the selected values display and information(checkSelected[])
         * */
        private void setText(int position1) {
            if (!checkSelected[position1]) {
                checkSelected[position1] = true;
                //selectedCount++;
            } else {
                checkSelected[position1] = false;
                //  selectedCount--;
            }
            selectedCount = getSelectedCount();
            if (selectedCount == 0) {
                mSelectedItems.setText(R.string.select_string);
            } else if (selectedCount == 1) {
                for (int i = 0; i < checkSelected.length; i++) {
                    if (checkSelected[i] == true) {
                        firstSelected = mListItems.get(i).getText();
                        break;
                    }
                }
                mSelectedItems.setText(firstSelected);
                setSelected(firstSelected);
            } else if (selectedCount > 1) {
                for (int i = 0; i < checkSelected.length; i++) {
                    if (checkSelected[i] == true) {
                        firstSelected = mListItems.get(i).getText();
                        break;
                    }
                }
                mSelectedItems.setText(firstSelected + " & " + (selectedCount - 1) + " more");
                setSelected(firstSelected + " & " + (selectedCount - 1) + " more");
            }
        }

        private int getSelectedCount() {
            int count = 0;
            for (int i = 0; i < checkSelected.length; i++) {
                if (checkSelected[i] == true) {
                    count++;
                }
            }
            return count;
        }

        private class ViewHolder {
            CheckBox chkbox;
        }
    }
}