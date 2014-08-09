package mohataher.github.com.db4o.android.demo;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import mohataher.github.com.db4o.android.demo.db.UserDao;
import mohataher.github.com.db4o.android.demo.db.domain.User;


public class MainActivity extends Activity {

    List<String> mListStr=new ArrayList<String>(10000);
    private ArrayAdapter<String> mListAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=getApplicationContext();
        if (savedInstanceState == null) {

            ListView listView= (ListView) findViewById(R.id.listView);
            mListAdapter=new ArrayAdapter<String>(mContext,R.layout.simple_list_item, mListStr);
            listView.setAdapter(mListAdapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        for (int i=0;i<100;i++){
            new SaveObjectOnDb4o(mContext).execute(null, null, null);
        }



    }


    public class SaveObjectOnDb4o extends AsyncTask<Void, Void, Void> {
        public SaveObjectOnDb4o(Context mContext) {

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                UserDao dao=new UserDao(mContext);
                dao.store(User.randomUser());

                dao.close();
                String str= "Storing two objects : success";
                mListStr.add(str);
            }catch (Throwable e){
                String str= "Storing two objects : FAILED !!!!!!!!!!!";
                mListStr.add(str);
                mListStr.add(e.getMessage());
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            super.onPostExecute(v);
            mListAdapter.notifyDataSetChanged();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
