package viewComponents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfinding.R;
import mapComponents.Room;

import java.io.File;
import java.util.ArrayList;

public class ConnectListAdapter extends RecyclerView.Adapter<ConnectListAdapter.ConnectViewHolder> {

    public class ConnectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ConnectListAdapter adapter;
        private TextView connectItemView;
        private Button connectButton;

        public ConnectViewHolder(View itemView, ConnectListAdapter adapter) {
            super(itemView);
            this.connectItemView = itemView.findViewById(R.id.connectRoom_name);
            this.adapter = adapter;
            this.connectButton = itemView.findViewById(R.id.connectItem_button);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface OnItemClickListener {
        void connect(int position);
    }

    private ConnectListAdapter.OnItemClickListener listener;

    public void setOnItemClickListener(ConnectListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


    private ArrayList<Room> roomList;
    private LayoutInflater inflater;

    public ConnectListAdapter(Context context, ArrayList<Room> roomList) {
        this.inflater = LayoutInflater.from(context);
        this.roomList = roomList;
    }
    @Override
    public ConnectListAdapter.ConnectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.connect_item, parent, false);
        return new ConnectListAdapter.ConnectViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(ConnectListAdapter.ConnectViewHolder holder, int position) {
        String roomName = this.roomList.get(position).getName();
        holder.connectItemView.setText(roomName);
        holder.connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.connect(holder.getBindingAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }
}
