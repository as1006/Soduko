package com.stars.soduko.ui.level;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.SPUtils;
import com.stars.base.ui.StarActivity;
import com.stars.kit.lego.adapter.bean.BaseBeanAdapter;
import com.stars.kit.lego.adapter.bean.BaseBeanItem;
import com.stars.kit.lego.adapter.bean.ItemBuilder;
import com.stars.kit.lego.adapter.bridge.BridgeEntity;
import com.stars.kit.lego.adapter.core.BaseItem;
import com.stars.kit.lego.adapter.core.BaseViewHolder;
import com.stars.kit.lego.layoutcenter.LayoutCenter;
import com.stars.soduko.R;
import com.stars.soduko.RecycleViewDivider;
import com.stars.soduko.model.SodukoPuzzle;
import com.stars.soduko.viewmodel.SodukoPuzzleViewModel;

public class LevelActivity extends StarActivity {

    private SodukoPuzzleViewModel mPuzzleViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        mPuzzleViewModel = getApplicationScopeViewModel(SodukoPuzzleViewModel.class);

        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LayoutCenter.getInstance().registerItemBuilder(SodukoPuzzle.class, new ItemBuilder<SodukoPuzzle>() {
            @Override
            public BaseItem build(Context context, SodukoPuzzle bean) {
                return new PuzzleItem(context, bean);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL));

        BaseBeanAdapter adapter = new BaseBeanAdapter(this);
        adapter.addBeans(mPuzzleViewModel.getAllPuzzles());
        recyclerView.setAdapter(adapter);

        adapter.getItemBridge().subscribe("Puzzle_Click", new BridgeEntity() {
            @Override
            public void onBridge(Object sender, String event, Object args) {
                int index = mPuzzleViewModel.getAllPuzzles().indexOf(args);
                if (index != -1) {
                    Intent intent = new Intent();
                    intent.putExtra("Puzzle", index);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });
    }


    public static class PuzzleItem extends BaseBeanItem<SodukoPuzzle> {

        public PuzzleItem(Context context, SodukoPuzzle bean) {
            super(context, bean);
        }

        @Override
        public int getLayoutId() {
            return R.layout.listitem_puzzle;
        }

        @Override
        public void onClick() {
            publishEvent("Puzzle_Click", bean);
        }

        @Override
        public void onBindViewHolder(@NonNull BaseViewHolder viewHolder, int position) {
            super.onBindViewHolder(viewHolder, position);

            TextView titleView = viewHolder.findViewById(R.id.tv_title);
            TextView stateView = viewHolder.findViewById(R.id.tv_state);

            int nonZeroCount = 0;
            for (int i = 0;i<bean.puzzle.length();i++) {
                if (bean.puzzle.charAt(i) != '0') {
                    nonZeroCount++;
                }
            }

            titleView.setText("关卡 "+(position+1)+" ("+nonZeroCount+")");

            boolean state = SPUtils.getInstance().getBoolean("puzzle_"+position+"_done", false);
            if (state) {
                stateView.setVisibility(View.VISIBLE);
            } else {
                stateView.setVisibility(View.INVISIBLE);
            }
        }
    }
}
