package com.example.AppArt.thaliapp.Calendar.Backend;

import android.content.Intent;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
import android.widget.ImageView;

import com.example.AppArt.thaliapp.Calendar.Activities.Calendar;
import com.example.AppArt.thaliapp.Calendar.Activities.Information;
import com.example.AppArt.thaliapp.R;
import com.example.AppArt.thaliapp.Settings.Backend.Database;

/**
 *
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */


public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    private SparseArray<Group> groups;
    public LayoutInflater inflater;
    public Calendar activity;

    /**
     *
     * @param calendar the calendar where it will be placed
     * @param groups the groups of the list
     */
    public MyExpandableListAdapter(Calendar calendar, SparseArray<Group> groups) {
        this.groups = groups;
        activity = calendar;
        inflater = calendar.getLayoutInflater();
    }

    public void setData(SparseArray<Group> groups) {
        this.groups = groups;
        notifyDataSetChanged();

    }

    /*****************************************************************
     All getters
     *****************************************************************/
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition << 16;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return (groupPosition << 16) | childPosition;
    }

    @Override
    public View getChildView(final int grouppos, final int childpos, boolean isLastChild, View convertView, final ViewGroup parent) {
        final ThaliaEvent children = (ThaliaEvent) getChild(grouppos, childpos);
        TextView text;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_details, parent, false);
        }
        final int plaats = index(grouppos, childpos);
        text = (TextView) convertView.findViewById(R.id.textView1);
        text.setText(children.makeSynopsis());
        ImageView img = (ImageView) convertView.findViewById(R.id.rowicon);
        img.setImageResource(children.getCatIcon());
        //An onclicklistener when a child is pressed, to go to the Information
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Information.class);
                intent.putExtra("event", Database.getDatabase().getEvents().get(plaats));
                v.getContext().startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_group, parent, false);
        }
        Group group = (Group) getGroup(groupPosition);
        ((TextView) convertView).setText(group.s);
        return convertView;
    }

    /**
     * Gives the image that corresponds with the category of the event
     *
     * @param s the given eventcategory
     * @return int: the number of the needed image
     */
    private int picture(EventCategory s) {
        switch (s) {
            case BORREL: {
                return R.drawable.borrelicoon;
            }
            case LECTURE: {
                return R.drawable.lezingicoon;
            }
            case ALV: {
                return R.drawable.alvicoon;
            }
            case PARTY: {
                return R.drawable.feesticoon;
            }
            case WORKSHOP: {
                return R.drawable.workshopicoon;
            }
            default:
                return R.drawable.overigicoon;
        }
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void setInflater(LayoutInflater inflater, Calendar calendar) {
        this.inflater = inflater;
        this.activity = calendar;
    }

    /**
     * Calculates the position of the event in the ArrayList events
     *
     * @param grouppos , number of the group
     * @param childpos , number of the child
     * @return the indexposition
     */
    private int index(int grouppos, int childpos) {
        int i = childpos;
        for (int j = 0; j < grouppos; j++) {
            i += getChildrenCount(j);
        }
        return i;
    }
}
