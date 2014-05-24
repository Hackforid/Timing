package com.smilehacker.timing.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.smilehacker.timing.R;
import com.smilehacker.timing.activity.SelectAppActivity;
import com.smilehacker.timing.adapter.CategoryListAdapter;
import com.smilehacker.timing.model.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kleist on 14-5-24.
 */
public class CategotyMenuFragment extends Fragment {

    private ListView mLvCategory;
    private Button mBtnAdd;
    private CategoryListAdapter mCategoryListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategoryListAdapter = new CategoryListAdapter(getActivity(), new ArrayList<Category>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_menu_category, container, false);
        mLvCategory = (ListView) view.findViewById(R.id.lv_category);
        mBtnAdd = (Button) view.findViewById(R.id.btn_add_category);

        mLvCategory.setAdapter(mCategoryListAdapter);
        initView();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        load();
    }

    private void initView() {
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText = new EditText(getActivity());
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                        .setView(editText)
                        .setTitle("New Category")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String categoryName = editText.getText().toString();
                                createCategory(categoryName);
                                dialogInterface.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                builder.create().show();
            }
        });

    }

    private void load() {
        new AsyncTask<Void, Void, List<Category>>() {

            @Override
            protected List<Category> doInBackground(Void... voids) {
                return Category.getAllCategories();
            }

            @Override
            protected void onPostExecute(List<Category> categories) {
                super.onPostExecute(categories);
                mCategoryListAdapter.flush(categories);
            }
        }.execute();
    }

    private void createCategory(String categoryName) {
        if (TextUtils.isEmpty(categoryName)) {
            return;
        }

        Category category = new Category();
        category.name = categoryName;
        category.save();
        load();
    }
}
