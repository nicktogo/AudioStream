package us.ktv.android.fragment;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import us.ktv.android.utils.OnItemClickListener;


public abstract class BaseListFragment<T> extends Fragment implements OnItemClickListener<T> {

//    protected OnFragmentInteractionListener mListener;

    public static final String ITEM_LAYOUT_ID = "item_layout_id";

    protected RecyclerView recyclerView;
    protected LinearLayoutManager linearLayoutManager;
    protected DataAdapter adapter;

    protected List<T> list;
    protected int itemLayoutId;

    public BaseListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(getLayoutId(), container, false);
        recyclerView = (RecyclerView) rootView.findViewById(android.R.id.list);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    protected void updateAdapter() {
        if (adapter == null) {
            adapter = new DataAdapter(itemLayoutId);
        }
        if (recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    protected abstract int getLayoutId();

    protected abstract int getVariable();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(Uri uri);
//    }

    protected class DataAdapter extends RecyclerView.Adapter<DataAdapter.ItemViewHolder> {

        private int itemLayoutId;

        public DataAdapter(int itemLayoutId) {
            this.itemLayoutId = itemLayoutId;
        }

        @Override
        public ItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int layoutId) {
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), itemLayoutId, viewGroup, false);
            return new ItemViewHolder(binding);
        }

        @Override
        public void onBindViewHolder(DataAdapter.ItemViewHolder holder, int position) {
            final T t = list.get(position);
            holder.binding.setVariable(getVariable(), t);
            holder.binding.executePendingBindings();
            holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BaseListFragment.this.onClick(t);
                }
            });
        }

        public class ItemViewHolder extends RecyclerView.ViewHolder {

            final ViewDataBinding binding;

            public ItemViewHolder(ViewDataBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }
        }

        protected int getVariable() {
            return BaseListFragment.this.getVariable();
        }

        @Override
        public int getItemCount() {
            return list.size();
        }
    }

}
