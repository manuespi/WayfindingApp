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

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.RoomViewHolder> {

    public class RoomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private RoomListAdapter adapter;
        private TextView roomItemView, nElems;
        private Button deleteRoomButton, editRoomButton;

        public RoomViewHolder(View itemView, RoomListAdapter adapter) {
            super(itemView);
            this.roomItemView = itemView.findViewById(R.id.room_name);
            this.nElems = itemView.findViewById(R.id.nElems_room);
            this.adapter = adapter;
            this.deleteRoomButton = itemView.findViewById(R.id.deleteRoom_button);
            this.editRoomButton = itemView.findViewById(R.id.editRoom_button);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface OnItemClickListener {
        void deleteRoom(int position);
        void editRoom(int position);
    }

    private RoomListAdapter.OnItemClickListener listener;

    public void setOnItemClickListener(RoomListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


    private ArrayList<Room> roomList;
    private LayoutInflater inflater;

    public RoomListAdapter(Context context, ArrayList<Room> roomList) {
        this.inflater = LayoutInflater.from(context);
        this.roomList = roomList;
    }
    @Override
    public RoomListAdapter.RoomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.room_item, parent, false);
        return new RoomListAdapter.RoomViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(RoomListAdapter.RoomViewHolder holder, int position) {
        String roomName = this.roomList.get(position).getName();
        holder.roomItemView.setText(roomName);
        holder.nElems.setText(Integer.toString(this.roomList.get(position).getnElements()));
        holder.deleteRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.deleteRoom(holder.getBindingAdapterPosition());
                }
            }
        });
        holder.editRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.editRoom(holder.getBindingAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }
}
