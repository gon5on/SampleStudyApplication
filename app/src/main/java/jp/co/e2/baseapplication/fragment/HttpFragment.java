package jp.co.e2.baseapplication.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.config.Config;
import jp.co.e2.baseapplication.entity.SampleEntity;
import jp.co.e2.baseapplication.http.SampleHttp;

/**
 * 非同期通信フラグメント
 *
 * 非同期通信を行うサンプル
 * イベントバスを使用しているので、画面回転に対応しているが、
 * イベントバスの登録と解除をonStartとonStopで行っているので、
 * 画面がバックグラウンドにいるときなどは、イベントバスからの通知を受け取れないので注意！
 */
public class HttpFragment extends Fragment {
    private static final String BUNDLE_RESULT = "bundle_result";

    private View mView;

    /**
     * ファクトリーメソッド
     *
     * @return fragment フラグメント
     */
    public static HttpFragment newInstance() {
        Bundle args = new Bundle();

        HttpFragment fragment = new HttpFragment();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_http, container, false);

        //イベントをセット
        setEvent();

        //再生成が走ったら、保管していた値を元に戻す
        if (savedInstanceState != null) {
            TextView textViewResult = (TextView) mView.findViewById(R.id.textViewResult);
            textViewResult.setText(savedInstanceState.getString(BUNDLE_RESULT));
        }

        return mView;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart() {
        super.onStart();

        // EventBusを登録する
        EventBus.getDefault().register(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStop() {
        // EventBusを登録解除する
        EventBus.getDefault().unregister(this);

        super.onStop();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //再生成が走る前に値を保管しておく
        TextView textViewResult = (TextView) mView.findViewById(R.id.textViewResult);
        outState.putString(BUNDLE_RESULT, textViewResult.getText().toString());
    }

    /**
     * イベントをセット
     */
    private void setEvent() {
        //成功が帰ってくるURLに接続
        mView.findViewById(R.id.buttonConnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi(Config.URL_SUCCESS);
            }
        });

        //エラーコードが帰ってくるURLに接続
        mView.findViewById(R.id.buttonConnectError).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi(Config.URL_ERROR);
            }
        });

        //存在しないURLに接続
        mView.findViewById(R.id.buttonDisconnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callApi(Config.URL_DISCONNECT);
            }
        });
    }

    /**
     * APIを呼ぶ
     */
    private void callApi(String url) {
        //結果表示を空にしておく
        TextView textViewResult = (TextView) mView.findViewById(R.id.textViewResult);
        textViewResult.setText(null);

        //APIに通信する
        new SampleHttp().execute(url);
    }

    /**
     * イベントバスのコールバックメソッド
     *
     * イベントバスは引数の型を見て、一致する型のコールバックメソッドに処理が戻る
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(SampleHttp.SampleEvent result) {
        //成功の場合、レスポンスを表示
        if (result.isSuccessful()) {
            showResponseData(result.getSampleApiEntity().getData());
        }
        //エラーの場合
        else {
            Toast.makeText(getContext(), getString(R.string.errorMsgSomethingError), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * レスポンスを表示
     *
     * @param data レスポンスデータ
     */
    private void showResponseData(ArrayList<SampleEntity> data) {
        String str = "";

        for (SampleEntity value : data) {
            str += value.getId() + "\n";
            str += value.getSample1() + "\n";
            str += value.getSample2() + "\n";
            str += value.getSample3() + "\n";
            str += value.getCreated() + "\n";
            str += value.getModified() + "\n\n";
        }

        TextView textViewResult = (TextView) mView.findViewById(R.id.textViewResult);
        textViewResult.setText(str);
    }
}
