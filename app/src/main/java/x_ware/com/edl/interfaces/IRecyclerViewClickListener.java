package x_ware.com.edl.interfaces;

import android.view.View;

/**
 * Created by buneavros on 2/21/18.
 */

public interface IRecyclerViewClickListener {
    void onClick(View view, int position, Object obj);
    void onLongClick(View view, int position, Object obj);
}
