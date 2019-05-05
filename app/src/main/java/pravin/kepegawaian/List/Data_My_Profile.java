package pravin.kepegawaian.List;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pravin.kepegawaian.Model.User;
import pravin.kepegawaian.R;

public class Data_My_Profile extends RecyclerView.Adapter<Data_My_Profile.ViewHolder> {
    private Context context;
    private List<User> users;

    public Data_My_Profile(Context context,List<User> users)
    {
        this.context = context;
        this.users = users;
    }

    @Override
    public Data_My_Profile.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_my_profile,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(Data_My_Profile.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.Nampeg.setText(user.getName());
        if (user.getGender().equals("L"))
        {
            holder.jeniskelamin.setText("Laki - Laki");
        }else
            {
                holder.jeniskelamin.setText("Perempuan");
            }
         holder.email.setText(user.getEmail());
        holder.divisi.setText(user.getDivision());
        holder.TTL.setText(user.getBirth_place() + "," + user.getBirth_date());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.NamaPegawai) TextView Nampeg;
        @BindView(R.id.JenisKelamin) TextView jeniskelamin;
        @BindView(R.id.Email) TextView email;
        @BindView(R.id.Divisi) TextView divisi;
        @BindView(R.id.TTL) TextView TTL;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
