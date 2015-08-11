package com.example.AppArt.thaliapp.Calendar.Backend;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Frank Gerlings (s4384873), Lisa Kalse (s4338340), Serena Rietbergen (s4182804)
 */

//TODO: SERENA, wil je de javadoc schrijven, Frank en ik snappen dit niet ;)
public class Group {
    public final List<String> children = new ArrayList<>();
    public String s;

    public Group(String string) {
        s = string;
    }
}
