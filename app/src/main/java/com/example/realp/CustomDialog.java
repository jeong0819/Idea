package com.example.realp;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomDialog {
    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        CustomDialog.count = count;
    }

    static int count=0;
    private Context context;
    addProjectC2 ac2=new addProjectC2();
    int tpc=0;
    public static int edsta=0; //1이면 설정에서수행
    Projectinfo p = new Projectinfo();
    public CustomDialog(Context context) {
        this.context = context;
    }

    public int getEdsta() {
        return edsta;
    }

    public void setEdsta(int edsta) {
        this.edsta = edsta;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.activity_show_friend_list);
        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        // 커스텀 다이얼로그의 각 위젯들을 정의한다.
        final ImageView iv_addName=(ImageView)dlg.findViewById(R.id.iv_getFName);
        final TextView member = dlg.findViewById(R.id.member);
        final EditText dlgEdt1 = (EditText) dlg.findViewById(R.id.dlgEdt1);
        final Spinner spinner=dlg.findViewById(R.id.teamSpin);
        final TextView dlgNag=dlg.findViewById(R.id.dlgNeg);
        final TextView dlgPos=dlg.findViewById(R.id.dlgPos);
        ArrayList<String> ar=new ArrayList<>();
            ar.add("팀장");
            ar.add("팀원");
            ArrayAdapter<String> adapter=new ArrayAdapter<>(dlg.getContext(),android.R.layout.simple_spinner_dropdown_item,ar);
        spinner.setAdapter(adapter);
        iv_addName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:친구목록 다이얼로그로 가져오기,member visible처리
            }
        });
      dlgNag.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              dlg.dismiss();
          }
      });
        dlgPos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:친구목록에서 가져온값으로 바꾸기
                String mName="이정현";
                if(getEdsta()==1){ //테스트용코드
                    mName="이정현4";
                }
                if(mName.equals("")){
                    Toast.makeText(context, "팀원을 선택해주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                String mChar=dlgEdt1.getText().toString();
                if(mChar.equals("")){
                    Toast.makeText(context, "역할을 적어주세요", Toast.LENGTH_SHORT).show();
                    return;
                }
                String mRank=spinner.getSelectedItem().toString();
                if(count>=1){
                   boolean isCheck= checkRank(mRank,getEdsta());
                   if(!isCheck){
                       return;
                   }
                }
                if(getEdsta()==0){
                    TeamData teamData=new TeamData(mName,mChar,mRank,R.drawable.clearmember,0,true);
                    p.pWorker.add(mName);
                    p.tChar.add(mChar);
                    p.tRank.add(mRank);
                    ac2.ar.add(teamData);
                    ac2.teamAdapter.notifyDataSetChanged();
                    tpc=ac2.ar.size();
                    p.setTpc(tpc);
                    count+=1;
                    dlg.dismiss();
                }else{
                    EditProject ep=new EditProject();
                    CurProjectinfo c=new CurProjectinfo();
                    Log.e("추가전",c.getTpc()+"");
                    TeamData wd=new TeamData(mName,mChar,mRank,R.drawable.clearmember,1,true);
                    ep.tar.add(wd);
                    ep.teamAdapter.notifyDataSetChanged();
                    c.worker.add(mName);ep.tmpuser.add(mName);
                    c.wChar.add(mChar);ep.tmpchar.add(mChar);
                    c.wRank.add(mRank);ep.tmprank.add(mRank);
                    c.setTpc(c.worker.size());
                    Log.e("추가후",c.getTpc()+"");
                    edsta=0;
                    count=0;
                    ep.setAddCount(ep.getAddCount()+1);
                    dlg.dismiss();
                }
            }
        });
    }
    private boolean checkRank(String rank,int psta){
        CurProjectinfo c =new CurProjectinfo();
        if(psta==1){
            for(int i=0;i<c.wRank.size();i++){
                String tmp=c.wRank.get(i);
                if(rank.equals("팀장")&&tmp.equals(rank)){
                    Toast.makeText(context, "팀장은 한명만 가능합니다.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }else{
            for(int i=0;i<p.tRank.size();i++){
                String tmp=p.tRank.get(i);
                if(rank.equals("팀장")&&tmp.equals(rank)){
                    Toast.makeText(context, "팀장은 한명만 가능합니다.", Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        }
        return true;
    }
}