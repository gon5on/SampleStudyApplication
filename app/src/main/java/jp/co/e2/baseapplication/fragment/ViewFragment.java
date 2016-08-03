package jp.co.e2.baseapplication.fragment;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.PopupMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import jp.co.e2.baseapplication.R;
import jp.co.e2.baseapplication.activity.SubActivity;
import jp.co.e2.baseapplication.dialog.BottomSheetDialog;
import jp.co.e2.baseapplication.dialog.SampleDialog;

/**
 * ビューとスタイルフラグメント
 * 画面回転にも対応したサンプルにもなっている
 */
public class ViewFragment extends Fragment implements SampleDialog.CallbackListener {
    private static final int TAG_DIALOG = 101;

    private View mView;

    /**
     * ファクトリーメソッド
     *
     * @return fragment フラグメント
     */
    public static ViewFragment newInstance() {
        Bundle args = new Bundle();

        ViewFragment fragment = new ViewFragment();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_view, container, false);

        //イベントをセットする
        setEvent();

        return mView;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    /**
     * イベントをセットする
     */
    private void setEvent() {
        //アクテビティを開く
        mView.findViewById(R.id.buttonActivity).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(SubActivity.newInstance(getActivity(), "hogehoge", 1000));
            }
        });

        //ダイアログを開く
        mView.findViewById(R.id.buttonDialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = getString(R.string.sample);
                String msg = getString(R.string.sampleDialog);
                String btn = getString(R.string.ok);
                SampleDialog sampleDialog = SampleDialog.getInstance(TAG_DIALOG, title, msg, btn);
                sampleDialog.setCallbackListener(ViewFragment.this);
                sampleDialog.show(getFragmentManager(), "dialog");
            }
        });

        //ポップアップメニューを開く
        mView.findViewById(R.id.buttonPopupMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(getActivity(), v);
                popup.getMenuInflater().inflate(R.menu.menu_popup_menu, popup.getMenu());
                popup.show();

            }
        });

        //ボトムシートを開く
        mView.findViewById(R.id.buttonBottomSheet).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog sampleDialog = BottomSheetDialog.getInstance();
                sampleDialog.show(getFragmentManager(), "dialog");
            }
        });

        //トースト表示
        mView.findViewById(R.id.buttonToast).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), getString(R.string.showToast), Toast.LENGTH_LONG).show();
            }
        });

        //スナックバーを表示
        //スナックバーの表示に併せてFABを上にあげる処理はしていない
        mView.findViewById(R.id.buttonSnackBar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar snackBar = Snackbar.make(v, getString(R.string.showSnackBar), Snackbar.LENGTH_LONG);
                snackBar.setActionTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
                snackBar.getView().setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimary));

                TextView textView = (TextView) snackBar.getView().findViewById(android.support.design.R.id.snackbar_text);
                textView.setTextColor(ContextCompat.getColor(getContext(), R.color.textLight));

                snackBar.setAction(getString(R.string.action), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //ボタンを押されたときに行いたい処理
                    }
                });
                snackBar.show();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClickSampleDialogOk(int tag) {
        Toast.makeText(getContext(), getString(R.string.ok), Toast.LENGTH_SHORT).show();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onClickSampleDialogCancel(int tag) {
        Toast.makeText(getContext(), getString(R.string.cancel), Toast.LENGTH_SHORT).show();
    }
}