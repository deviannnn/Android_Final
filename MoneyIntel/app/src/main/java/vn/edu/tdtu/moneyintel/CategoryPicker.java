package vn.edu.tdtu.moneyintel;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

import vn.edu.tdtu.moneyintel.adapter.CategoryAdapter;
import vn.edu.tdtu.moneyintel.database.DatabaseHelper;
import vn.edu.tdtu.moneyintel.model.Category;

public class CategoryPicker extends AppCompatActivity {
    ImageView imgvBack;
    CategoryAdapter categoryAdapter;
    RecyclerView rvCategoryList;
    DatabaseHelper dbHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_picker);

        imgvBack = findViewById(R.id.imgvBack);
        imgvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadCategoryList();
    }

    private void loadCategoryList() {
        rvCategoryList = findViewById(R.id.rvCategoryList);
        rvCategoryList.setLayoutManager(new LinearLayoutManager(this));

        List<Category> categoryList = dbHelper.getAllCategories();

        categoryAdapter = new CategoryAdapter(categoryList);
        rvCategoryList.setAdapter(categoryAdapter);

        categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Category selectedCategory = categoryList.get(position);

                Intent intent = new Intent();
                intent.putExtra("categoryId", selectedCategory.getId());
                intent.putExtra("categoryName", selectedCategory.getName());
                intent.putExtra("categoryType", selectedCategory.getType());
                intent.putExtra("categoryIcon", selectedCategory.getIcon());

                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
}