package library;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.earthgee.residemenu.R;

/**
 * Created by earthgee on 16/10/16.
 */
public class ResideMenuItem extends LinearLayout{

    private ImageView ivIcon;
    private TextView tvTitle;

    public ResideMenuItem(Context context) {
        super(context);
        initViews(context);
    }

    public ResideMenuItem(Context context,int icon,String text){
        super(context);
        initViews(context);
        ivIcon.setImageResource(icon);
        tvTitle.setText(text);
    }

    private void initViews(Context context){
        LayoutInflater inflater=LayoutInflater.from(context);
        inflater.inflate(R.layout.reside_item,this);
        ivIcon= (ImageView) findViewById(R.id.iv_icon);
        tvTitle= (TextView) findViewById(R.id.tv_title);
    }

}
