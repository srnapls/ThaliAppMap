package com.example.AppArt.thaliapp.Calendar.Backend;

import android.content.Intent;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.example.AppArt.thaliapp.Calendar.Activities.Calendar;
import com.example.AppArt.thaliapp.Calendar.Activities.Information;
import com.example.AppArt.thaliapp.R;

/**
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */

//TODO: Serena, Javadoc pl0x?
public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    private final SparseArray<Group> groups;
    public LayoutInflater inflater;
    public Calendar activity;
    private EventCategory[] info;

    public MyExpandableListAdapter(Calendar calendar, SparseArray<Group> groups, EventCategory[] info) {
        this.groups = groups;
        activity = calendar;
        inflater = calendar.getLayoutInflater();
        this.info = info;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public View getChildView(final int grouppos, final int childpos, boolean isLastChild, View convertView, final ViewGroup parent) {
        final String children = (String) getChild(grouppos, childpos);
        TextView text;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_details, null);
        }
        int plaats = index(grouppos, childpos);
        text = (TextView) convertView.findViewById(R.id.textView1);
        text.setText(children);
        if (info != null) {
            text.setCompoundDrawablesWithIntrinsicBounds(picture(info[plaats]), 0, 0, 0);
        } else {
            text.setCompoundDrawablesWithIntrinsicBounds(R.drawable.overigicoon, 0, 0, 0);
        } //An onclicklistener when a child is pressed.
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = index(grouppos, childpos);
                Intent intent = new Intent(v.getContext(), Information.class);
                intent.putExtra("index", i);
                v.getContext().startActivity(intent);
            }
        });
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
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listrow_group, null);
        }
        Group group = (Group) getGroup(groupPosition);
        ((CheckedTextView) convertView).setText(group.s);
        ((CheckedTextView) convertView).setChecked(isExpanded);
        return convertView;
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
     * calculates the position of the event in the ArrayList events
     *
     * @param grouppos , number of the group
     * @param childpos , number of the child
     * @return the indexposition
     */
    private int index(int grouppos, int childpos) {
        int i = 0, j = 0, p;
        while (j < getGroupCount()) {
            p = 0;
            while (p < getChildrenCount(j)) {
                if (j == grouppos && p == childpos) {
                    break;
                }
                p++;
                i++;
            }
            if (j == grouppos && p == childpos)
                break;
            j++;
        }
        return i;
    }
}
