package viewComponents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wayfinding.R;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class MapFileListAdapter extends RecyclerView.Adapter<MapFileListAdapter.MapFileViewHolder> {

    public class MapFileViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MapFileListAdapter adapter;
        private TextView mapItemView;
        private Button deleteMapButton, editMapButton, playMapButton;

        //Añadir los botones supongo

        public MapFileViewHolder(View itemView) { //antes estaba comentado
            super(itemView);
        }

        public MapFileViewHolder(View itemView, MapFileListAdapter adapter) {
            super(itemView);
            this.mapItemView = itemView.findViewById(R.id.mapFile_name);
            this.adapter = adapter;
            this.deleteMapButton = itemView.findViewById(R.id.deleteMap_button);
            this.editMapButton = itemView.findViewById(R.id.editMap_button);
            this.playMapButton = itemView.findViewById(R.id.playMap_button);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface OnItemClickListener {
        void deleteMap(int position);
        void editMap(int position);
        void playMap(int position);
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    private ArrayList<File> fileList;
    private LayoutInflater inflater;

    public MapFileListAdapter(Context context, ArrayList<File> fileList) {
        this.inflater = LayoutInflater.from(context);
        this.fileList = fileList;
    }
    @Override
    public MapFileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.map_file_item, parent, false);
        return new MapFileViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(MapFileViewHolder holder, int position) {
        String fileName = this.fileList.get(position).getName();
        fileName = fileName.replace(".json", "");
        holder.mapItemView.setText(fileName);
        holder.deleteMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                   // listener.deleteMap(holder.getBindingAdapterPosition());
                }
            }
        });
        holder.editMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    //listener.editMap(holder.getBindingAdapterPosition());
                }
            }
        });

        holder.playMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                  //  listener.playMap(holder.getBindingAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }
}
