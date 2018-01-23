package com.ngame.app;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.lx.pad.R;
import com.lx.pad.util.LLog;
import com.ngame.DabaBase.KeyEnum;

/**
 * Created by Administrator on 2017/12/2.
 */

public abstract class BaseKeyViewFragment extends BaseFragment {
    protected ViewDragInterface viewDragInterface;
    private boolean m_isPress = true;

    public BaseKeyViewFragment() {
        super();
    }

    public final void setViewDragInterface(ViewDragInterface viewDragInterface){
        this.viewDragInterface = viewDragInterface;
    }

    public abstract void setImgPressState(ImageView view);

    public final boolean viewMove(View view, MotionEvent motionEvent){
        LLog.d("BaseKeyViewFragment->viewMove action:" + motionEvent.getAction());
        switch(motionEvent.getAction()){
            case MotionEvent.ACTION_DOWN:{
                m_isPress = true;
                setImgPressState((ImageView)view);
                break;
            }
            case MotionEvent.ACTION_MOVE:{
                if(!m_isPress){
                    return true;
                }
                m_isPress = false;
                if(viewDragInterface != null){
                    int id = view.getId();
                    switch(id){
                        case R.id.iv_key_a:view.setTag(KeyEnum.A);break;
                        case R.id.iv_key_b:view.setTag(KeyEnum.B);break;
                        case R.id.iv_key_x:view.setTag(KeyEnum.X);break;
                        case R.id.iv_key_y:view.setTag(KeyEnum.Y);break;
                        case R.id.iv_key_up:view.setTag(KeyEnum.UP);break;
                        case R.id.iv_key_down:view.setTag(KeyEnum.DOWN);break;
                        case R.id.iv_key_left:view.setTag(KeyEnum.LEFT);break;
                        case R.id.iv_key_right:view.setTag(KeyEnum.RIGHT);break;
                        case R.id.iv_key_r1:view.setTag(KeyEnum.R1);break;
                        case R.id.iv_key_r2:view.setTag(KeyEnum.R2);break;
                        case R.id.iv_key_l1:view.setTag(KeyEnum.L1);break;
                        case R.id.iv_key_l2:view.setTag(KeyEnum.L2);break;
                        case R.id.iv_key_start:view.setTag(KeyEnum.START);break;
                        case R.id.iv_key_back:view.setTag(KeyEnum.BACK);break;
                        case R.id.iv_key_r:view.setTag(KeyEnum.R);break;
                        case R.id.iv_key_direction:view.setTag(KeyEnum.F);break;
                        default:{
                            LLog.d("BaseKeyViewFragment->viewMove ACTION_MOVE default");
                            break;
                        }
                    }

                    viewDragInterface.setCurDragView((ImageView)view);
                    if(viewDragInterface.isViewDraged((ImageView)view)){
                        view.startDrag(null, new View.DragShadowBuilder(view), null, 0);
                    }

                    sSetKeyUpImgView((ImageView)view);
                    return true;
                }
                view.startDrag(null, new View.DragShadowBuilder(view), null, 0);
                break;
            }
            case MotionEvent.ACTION_CANCEL:{
                sSetKeyUpImgView((ImageView)view);
                break;
            }
        }
        return true;
    }

    public static void setPressImgView(ImageView view){
        int vId = view.getId();
        switch(vId){
            case R.id.iv_key_a:view.setImageResource(R.mipmap.ic_key_a_press);break;
            case R.id.iv_key_b:view.setImageResource(R.mipmap.ic_key_b_press);break;
            case R.id.iv_key_x:view.setImageResource(R.mipmap.ic_key_x_press);break;
            case R.id.iv_key_y:view.setImageResource(R.mipmap.ic_key_y_press);break;
            case R.id.iv_key_up:view.setImageResource(R.mipmap.ic_key_up_press);break;
            case R.id.iv_key_down:view.setImageResource(R.mipmap.ic_key_down_press);break;
            case R.id.iv_key_left:view.setImageResource(R.mipmap.ic_key_left_press);break;
            case R.id.iv_key_right:view.setImageResource(R.mipmap.ic_key_right_press);break;
            case R.id.iv_key_r1:view.setImageResource(R.mipmap.ic_key_r1_press);break;
            case R.id.iv_key_r2:view.setImageResource(R.mipmap.ic_key_r2_press);break;
            case R.id.iv_key_l1:view.setImageResource(R.mipmap.ic_key_l1_press);break;
            case R.id.iv_key_l2:view.setImageResource(R.mipmap.ic_key_l2_press);break;
            case R.id.iv_key_start:view.setImageResource(R.mipmap.ic_key_start_press);break;
            case R.id.iv_key_back:view.setImageResource(R.mipmap.ic_key_back_press);break;
            case R.id.iv_key_r:view.setImageResource(R.mipmap.ic_key_r_press);break;
            case R.id.iv_key_direction:view.setImageResource(R.mipmap.ic_key_direction_press);break;
            default:{
                LLog.d("BaseKeyViewFragment->setPressImgView default");
                break;
            }
        }
    }

    public static void sSetKeyUpImgView(ImageView view){
        int vId = view.getId();
        switch(vId){
            case R.id.iv_key_a:view.setImageResource(R.mipmap.ic_key_a);break;
            case R.id.iv_key_b:view.setImageResource(R.mipmap.ic_key_b);break;
            case R.id.iv_key_x:view.setImageResource(R.mipmap.ic_key_x);break;
            case R.id.iv_key_y:view.setImageResource(R.mipmap.ic_key_y);break;
            case R.id.iv_key_up:view.setImageResource(R.mipmap.ic_key_up);break;
            case R.id.iv_key_down:view.setImageResource(R.mipmap.ic_key_down);break;
            case R.id.iv_key_left:view.setImageResource(R.mipmap.ic_key_left);break;
            case R.id.iv_key_right:view.setImageResource(R.mipmap.ic_key_right);break;
            case R.id.iv_key_r1:view.setImageResource(R.mipmap.ic_key_r1);break;
            case R.id.iv_key_r2:view.setImageResource(R.mipmap.ic_key_r2);break;
            case R.id.iv_key_l1:view.setImageResource(R.mipmap.ic_key_l1);break;
            case R.id.iv_key_l2:view.setImageResource(R.mipmap.ic_key_l2);break;
            case R.id.iv_key_start:view.setImageResource(R.mipmap.ic_key_start);break;
            case R.id.iv_key_back:view.setImageResource(R.mipmap.ic_key_back);break;
            case R.id.iv_key_r:view.setImageResource(R.mipmap.ic_key_r);break;
            case R.id.iv_key_direction:view.setImageResource(R.mipmap.ic_key_direction);break;
            default:{
                LLog.d("BaseKeyViewFragment->sSetKeyUpImgView default");
                break;
            }
        }
    }
}
