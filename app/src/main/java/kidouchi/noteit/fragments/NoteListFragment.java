package kidouchi.noteit.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.sql.SQLException;

import butterknife.Bind;
import butterknife.ButterKnife;
import kidouchi.noteit.R;
import kidouchi.noteit.activity.NoteEditorActivity;
import kidouchi.noteit.db.AppDatabase;
import kidouchi.noteit.note.Note;
import kidouchi.noteit.note.NoteAdapter;

public class NoteListFragment extends Fragment {

    public static final String NOTE = "NOTE";
    private Note[] mNotes;
    private AppDatabase mDB = AppDatabase.getInstance(getActivity());
    private NoteAdapter adapter;
    private RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();
            updateViewOnEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            updateViewOnEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            updateViewOnEmpty();
        }
    };

    @Bind(R.id.note_recycler_view) RecyclerView mNoteRecyclerView;
    @Bind(R.id.edit_fab) FloatingActionButton mEditFabButton;
    @Bind(R.id.empty_note_view) TextView mEmptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.note_list_fragment, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Parcelable[] parcels = getArguments().getParcelableArray("NoteArray");
        mNotes = new Note[parcels.length];
        for (int i = 0; i < mNotes.length; i++) {
            mNotes[i] = (Note)parcels[i];
        }
        // Setup the Note List Layout
        adapter = new NoteAdapter(mNotes, getActivity(), mDB);
        mNoteRecyclerView.setAdapter(adapter);

        // Update view when empty or not
        adapter.registerAdapterDataObserver(observer);
        adapter.notifyDataSetChanged();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mNoteRecyclerView.setLayoutManager(layoutManager);
        mNoteRecyclerView.setHasFixedSize(false);

        // Setup the Note Edit Button
        mEditFabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoteEditorActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateViewOnEmpty() {
        if (adapter.getItemCount() == 0) {
            mNoteRecyclerView.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mNoteRecyclerView.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            mDB.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mDB.close();
        adapter.unregisterAdapterDataObserver(observer);
    }
}
