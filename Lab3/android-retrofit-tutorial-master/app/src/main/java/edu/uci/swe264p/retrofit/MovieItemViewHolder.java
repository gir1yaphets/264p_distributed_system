package edu.uci.swe264p.retrofit;

import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieItemViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> viewList;

    public MovieItemViewHolder(@NonNull View itemView) {
        super(itemView);
        viewList = new SparseArray<>();
    }

    public View getViewById(@IdRes int viewId) {
        View view = viewList.get(viewId);
        if (view == null) {
            view = itemView.findViewById(viewId);
            viewList.put(viewId, view);
        }

        return view;
    }

    public void setText(@IdRes int viewId, String text) {
        if (text != null) {
            TextView textView = (TextView) getViewById(viewId);
            textView.setText(text);
        }
    }
}
