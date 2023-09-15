package viewComponents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfinding.R;

import java.util.ArrayList;

import mapComponents.Element;
import mapComponents.Room;

public class ElementListAdapter extends RecyclerView.Adapter<ElementListAdapter.ElementViewHolder> {

    public class ElementViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ElementListAdapter adapter;
        private TextView elementItemView, wallType;
        private Button deleteElementButton;

        public ElementViewHolder(View itemView, ElementListAdapter adapter) {
            super(itemView);
            this.elementItemView = itemView.findViewById(R.id.elem_type);
            this.wallType = itemView.findViewById(R.id.wall_type);
            this.adapter = adapter;
            this.deleteElementButton = itemView.findViewById(R.id.deleteElem_button);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface OnItemClickListener {
        void deleteElement(int position);
    }

    private ElementListAdapter.OnItemClickListener listener;

    public void setOnItemClickListener(ElementListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


    private ArrayList<Element> elementList;
    private LayoutInflater inflater;

    public ElementListAdapter(Context context, ArrayList<Element> elementList) {
        this.inflater = LayoutInflater.from(context);
        this.elementList = elementList;
    }
    @Override
    public ElementListAdapter.ElementViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.element_item, parent, false);

        return new ElementListAdapter.ElementViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(ElementListAdapter.ElementViewHolder holder, int position) {
        String elemType = this.elementList.get(position).getType();
        holder.elementItemView.setText(elemType);
        holder.wallType.setText(this.elementList.get(position).orientationString());
        holder.deleteElementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.deleteElement(holder.getBindingAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return elementList.size();
    }
}
