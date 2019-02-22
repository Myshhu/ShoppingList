package com.example.shoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> listMessages;
    private ArrayList<Integer> listIsSelected;

    public ListAdapter(Context context, ArrayList<String> listMessages, ArrayList<Integer> listIsSelected) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listMessages = listMessages;
        this.listIsSelected = listIsSelected;
    }

    @Override
    public int getCount() {
        return listMessages.size();
    }
;
    @Override
    public Object getItem(int position) {
        return listMessages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        TextView tView;
        ImageView iView;

        //System.out.println("called getView, position: " + position);
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.custom_listview_item, null);
        }

        final View finalConvertView = convertView;
        System.out.println("Setting click listener");
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch colors
                listIsSelected.set(position, listIsSelected.get(position) == 0 ? 1 : 0);
                //Color convertView onClick
                if (listIsSelected.get(position) == 1) {
                    finalConvertView.setBackgroundColor(ContextCompat.getColor(context, R.color.darkGreen));
                } else {
                    finalConvertView.setBackgroundColor(ContextCompat.getColor(context, R.color.colour_highlight_grey));
                }

                if(areAllSelected(listIsSelected)){
                    showDialog();
                }
            }
        });

        //Refresh colors after swiping
        if (listIsSelected.get(position) == 1) {
            finalConvertView.setBackgroundColor(ContextCompat.getColor(context, R.color.darkGreen));
        } else {
            finalConvertView.setBackgroundColor(ContextCompat.getColor(context, R.color.colour_highlight_grey));
        }

        tView = convertView.findViewById(R.id.tView);
        tView.setText(listMessages.get(position));

        iView = convertView.findViewById(R.id.iView);
        iView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Disable dialog when removing items from finished list
                boolean finishedBeforeRemoving = areAllSelected(listIsSelected);
                listMessages.remove(position);
                listIsSelected.remove(position);
                notifyDataSetChanged();
                Toast.makeText(context, "Delete clicked " + position, Toast.LENGTH_SHORT).show();
                if(areAllSelected(listIsSelected) && !finishedBeforeRemoving){
                    showDialog();
                }
            }
        });

        System.out.println("Position: " + position);
        return convertView;
    }

    private boolean areAllSelected(ArrayList<Integer> list){
        for (int a : list) {
            if(a == 0) {
                return false;
            }
        }
        return true;
    }

    private void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Finished shopping");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }
}
