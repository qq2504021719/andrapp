package com.bignerdranch.android.criminalintent;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Administrator on 2017/8/7.
 */

public class CrimeFragment extends Fragment{

    // argument bundle key
    private static final String ARG_CRIME_ID = "crime_id";
    // DatePickerFragment 标签
    private static final String DIALOG_DATE = "DialogDate";
    // 请求代码常量 短信
    private static final int REQUEST_DATE = 0;
    // 请求代码常量 获取联系人
    private static final int REQUEST_CONTACT = 1;
    // 请求代码常量 获取联系人电话
    private static final int REQUEST_PHONE = 2;
    // 请求代码常量 相机拍照
    private static final int REQUEST_PHOTO = 3;

    private Crime mCrime;
    // 标题
    private EditText mTitleField;
    // is解决
    private CheckBox mSolvedCheckBox;
    // 时间
    private EditText mDateedittext;
    // 保存按钮,返回列表页
    private Button mcrimesaveButton;
    // 当前UUID
    private UUID crimeId;
    // 发送短信
    private Button mReportButton;
    // 获取联系人
    private Button mSuspectButton;
    // 拨打联系人电话
    private Button mPhoneButton;
    // 显示值
    private String phoneNumber = "未找到嫌疑人号码";
    // 选择图片按钮
    private ImageButton mPhotoButton;
    // 图片缩略图显示
    private ImageView mPhotoView;
    // 图片弹出显示
    private ImageView mDialogPhotoView;
    // 图片存储位置
    private File mPhotoFile;
    
    // 回调函数存储变量
    private Callbacks mCallbacks;

    /**
     * 实现回调接口
     */
    public interface Callbacks{
        void onCrimeUpdated(Crime crime);
    }




    /*
    *
    * 要附加argument bundle给fragment,需要调用Fragment.setArguments(Bundle)方法。而且，
    * 还要在fragment创建后,添加给activity前完成
    *
    * 托管activity需要fragment实例时,会调用newInstance()方法,而非直接调用其构造方法。
    * */
    public static CrimeFragment newInstance(UUID crimeId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_CRIME_ID,crimeId);

        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        // 让FragmentManager知道CrimeFragment需接收选项菜单方法回调
        setHasOptionsMenu(true);

        // 获取用户点击列的id
        crimeId = (UUID) getArguments().getSerializable(ARG_CRIME_ID);
        // 根据列id获取详细信息
        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        // 获取图片存储位置
        mPhotoFile = CrimeLab.get(getActivity()).getPhotoFile(mCrime);
    }

    /*
    *
    *
    * 用户可能会在CrimeFragment中修改Crime实例，修改完成后,我们需要刷新CrimeLab中的Crime数据,这可以通过在CrimeFragment.java中
    * 覆盖CrimeFragment.onPause()方法完成
    *
    * */
    @Override
    public void onPause(){
        super.onPause();
        CrimeLab.get(getActivity())
                .updateCrime(mCrime);
    }

    @Override
    public void onDetach(){
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    /*
    * 创建视图 等同于Activity.onCreate()的方法处理
    * */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        // 创建fragment_crime视图
        View v = inflater.inflate(R.layout.fragment_crime,container,false);

        // 设置EditText监听方法
        mTitleField = (EditText)v.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // 这个空间故意留空
            }

            /*
            * 调用charSequence(代表用户输入)的toString()方法,该方法最后返回用来设置Crime标题的字符串。
            * */
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mCrime.setTitle(charSequence.toString());

                updateCrime();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



        mDateedittext = (EditText) v.findViewById(R.id.crime_data_edittext);
        // 点击显示日期对话框
        mDateedittext.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
                // 将CrimeFragment设置成DatePickerFragment的目标fragment,DatePickerFragment回传数据给CrimeFragment
                dialog.setTargetFragment(CrimeFragment.this,REQUEST_DATE);
                // 显示日历选择器
                dialog.show(manager,DIALOG_DATE);
            }
        });
        UpdateButtonText();


        // 设置监听用于更新Crime的mSolved变量值
        mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.isSolved());
        // 设置监听
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked){
                // 设置crime的solved的变量值
                mCrime.setSolved(isChecked);
                updateCrime();
            }
        });

        // 监听发送短信按钮
        mReportButton = (Button)v.findViewById(R.id.crime_report);
        mReportButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                // 隐试Intent启动短信
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT,getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT,R.string.crime_report_subject);
                // 有多个可选择的话,始终出现列表让用户选择
                i = Intent.createChooser(i,getString(R.string.send_report));
                startActivity(i);
            }
        });

        // 监听获取联系人列表
        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        mSuspectButton = (Button) v.findViewById(R.id.crime_suspect);
        mSuspectButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                startActivityForResult(pickContact,REQUEST_CONTACT);
            }
        });
        // 显示联系人 姓名
        if(mCrime.getSuspect() != null){
            mSuspectButton.setText(mCrime.getSuspect());
        }


        // 联系嫌疑人
        mPhoneButton = (Button)v.findViewById(R.id.crime_phone);
        mPhoneButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mCrime.getPhone() != null && !mCrime.getPhone().equals(phoneNumber)){

                    // 启动电话拨打界面
                    Intent i = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+mCrime.getPhone()));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);

                }else{
                    Toast.makeText(getActivity(),R.string.crime_phone_cuo_wu,Toast.LENGTH_SHORT).show();
                }

            }
        });

        // 显示联系人 电话
        if(mCrime.getPhone() != null && !mCrime.getPhone().equals(phoneNumber)){
            mPhoneButton.setText("拨打:"+mCrime.getPhone());
        }


        // android设备上安装了那些组件以及那些activity,PackageManager类全部都知道。调用resolveActivity(Intent,int)方法
        // 我们可以找到匹配给定Intent任务的activity,flag标志MATCH_DEFAULT_ONLY限定只搜索带CATEGORY_DEFAULT标志的Activity
        // 搜到目标,他会返回RESULVEInfo告诉你找到了哪个activity。如果找不到,必须禁用嫌疑人按钮,否则应用会崩溃
        PackageManager packageManager = getActivity().getPackageManager();
        // 查询是否拥有短信类应用,否则禁用发送短信
        if (packageManager.resolveActivity(pickContact,PackageManager.MATCH_DEFAULT_ONLY) == null){
            mSuspectButton.setEnabled(false);
        }


        // 细节
        // 图片缩略图
        mPhotoView = (ImageView)v.findViewById(R.id.crime_photo);
        // 图片弹出显示
        mDialogPhotoView = (ImageView)v.findViewById(R.id.dialog_crime_photo);
        // 刷新显示图片
        updatePhotoView();
        // 点击查看大图
        mPhotoView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                updateDialogPhotoView();

            }
        });

        // 图片选择按钮
        mPhotoButton = (ImageButton)v.findViewById(R.id.crime_camera);
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 文件存储地址不能为空,相机类应用不能为空
        boolean canTakePhoto = mPhotoFile != null && captureImage.resolveActivity(packageManager) != null;
        mPhotoButton.setEnabled(canTakePhoto);

        if(canTakePhoto){
            Uri uri = Uri.fromFile(mPhotoFile);
            captureImage.putExtra(MediaStore.EXTRA_OUTPUT,uri);
        }

        mPhotoButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 启动相机应用
                startActivityForResult(captureImage,REQUEST_PHOTO);
            }
        });

        // 保存按钮,返回
        mcrimesaveButton = (Button)v.findViewById(R.id.crime_save);
        mcrimesaveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getActivity().finish();
            }
        });

        return v;
    }

    /*
    *
    * 将布局文件中的菜单项目填充到menu中
    *
    * */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu,inflater);
        inflater.inflate(R.menu.fragment_crime,menu);
    }

    /*
    *
    * 点击菜单栏
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_crime_delete:
                CrimeLab.get(getActivity()).deleteCrime(crimeId);
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*
    * 设置button显示时间
    * */
    private void UpdateButtonText(){
        // 设置显示时间
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        mDateedittext.setText(sDateFormat.format(mCrime.getDate()));
    }

    /*
    * 覆盖onActivityResult方法,从extra中获取数据
    * */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode != Activity.RESULT_OK){
            return;
        }
        // 日期弹出返回用户选择日期
        if(requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setDate(date);
            updateCrime();
            UpdateButtonText();
        // 联系人选择列表返回一个uri数据定位符,指向用户所选联系人
        }else if(requestCode == REQUEST_CONTACT && data != null){
            //创建一天查询语句,要求返回全部联系人的名字,然后查询联系人数据库,获得一个可用的Cursor。
            // 因为已经知道Cursor只包含一条记录,所有将Cursor移动到第一条记录并获取它的字符串形式。
            // 该字符串即为嫌疑人的姓名。然后。使用它设置Crime嫌疑人,并显示在CHOOSE SUSPECT按钮上。


            Uri contactUri = data.getData();
            // 查询联系人名称
            String[] queryFields = new String[]{
                    // 查询选择的联系人的名称
                    ContactsContract.Contacts.DISPLAY_NAME,
                    ContactsContract.Contacts._ID
            };
            Cursor c = getActivity().getContentResolver().query(contactUri,queryFields,null,null,null);


            try{
                if(c.getCount() == 0){
                    return;
                }



                c.moveToFirst();
                // 获得联系人名称
                String suspect = c.getString(0);
                // 保存联系人名称
                mCrime.setSuspect(suspect);
                // 显示联系人名称
                mSuspectButton.setText(suspect);


                Cursor cp = getActivity().getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID+" =?",
                    new String[]{c.getString(1)},
                    null);
                String phoneNumbers = phoneNumber;
                // 获取手机号码
                if(cp.moveToFirst()){
                    // 获取电话号码
                    phoneNumbers = cp.getString(cp.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }
                // 保存电话号码
                mCrime.setPhone(phoneNumbers);
                // 显示电话号码
                if(phoneNumbers.equals(phoneNumber)){
                    mPhoneButton.setText(phoneNumbers);
                }else{
                    mPhoneButton.setText("拨打:"+phoneNumbers);
                }
                updateCrime();
                cp.close();
            }finally {
                c.close();
            }
        // 启动相机Activity返回
        }else if(requestCode == REQUEST_PHOTO){
            updateCrime();
            // 刷新显示图片
            updatePhotoView();
        }
    }

    /**
     * 在详情页中,如果标题,日期，是否解决有变动,则调用此方法。刷新列表页的数据
     * 在crimeLab中保存mCrime
     * 调用mCallbacks.onCrimeUpdated(Crime)
     */
    private void updateCrime(){
        CrimeLab.get(getActivity()).updateCrime(mCrime);
        mCallbacks.onCrimeUpdated(mCrime);
    }

    /**
     * 评接消息模板
     */
    private String getCrimeReport(){
        // 是否解决
        String solvedString = null;
        if(mCrime.isSolved()){
            solvedString = getString(R.string.crime_report_solved);
        }else{
            solvedString = getString(R.string.crime_report_unsolved);
        }

        // 发送时间
        String dateFormat = "yyyy年MM月dd日";
        SimpleDateFormat sDateFormat = new SimpleDateFormat(dateFormat);
        String dateString = sDateFormat.format(mCrime.getDate());

        // 嫌疑人
        String suspect = mCrime.getSuspect();
        if (suspect == null){
            suspect = getString(R.string.crime_report_no_suspect);
        }else{
            suspect = getString(R.string.crime_report_suspect,suspect);
        }

        // 标题
        String report = getString(R.string.crime_report,mCrime.getTitle(),
                dateString,suspect,solvedString);

        return report;
    }

    /**
     * 刷新mPhotoView的值方法
     */
    private void updatePhotoView(){
        if(mPhotoFile == null || !mPhotoFile.exists()){
            mPhotoView.setImageDrawable(null);
        }else{
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    /**
     * 点击缩略图显示大图
     */
    public void updateDialogPhotoView(){
        // 获取模板
        View vLayout = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_image,null);

        mDialogPhotoView = (ImageView)vLayout.findViewById(R.id.dialog_crime_photo);

        // 设置图片
        Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(),getActivity());
        mDialogPhotoView.setImageBitmap(bitmap);

        // 弹出显示
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.dialog_photo_title)
                .setView(vLayout)
                .setPositiveButton(android.R.string.ok,null)
                .create();
        alertDialog.show();

    }
}
