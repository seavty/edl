package x_ware.com.edl.modules.project;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import static maes.tech.intentanim.CustomIntent.customType;
import x_ware.com.edl.R;
import x_ware.com.edl.adapters.ViewPagerAdapter;
import x_ware.com.edl.networking.models.project.ProjectViewModel;

public class ProjectDetailActivity extends AppCompatActivity {
    private static final String TAG = "ProjectDetailActivity";

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    //private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    ViewPagerAdapter myAdapter;

    private ProjectViewModel project;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    private TextView lblProjectName;

    private int[] tabIcons = {
            R.drawable.ic_business_white_24px,
            R.drawable.ic_settings_white_24px,
            R.drawable.ic_rss_feed_white_24px
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_detail);

        initializeComponents();


//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


    }

    // *** private methods *** /
    //-> initializeComponents()
    private void initializeComponents(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mViewPager = findViewById(R.id.container);
        setupViewPager();

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        setupTabIcons();
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        lblProjectName = findViewById(R.id.lblProjectName);


        if(getIntent() != null && getIntent().hasExtra("ProjectViewModel")) {
            project = (ProjectViewModel) getIntent().getSerializableExtra("ProjectViewModel");
            lblProjectName.setText(project.description);
        }

    }

    private void setupViewPager() {
        myAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        myAdapter.addFragments(new ProjectCompanyFragment(), "comp");
        myAdapter.addFragments(new ProjectSpecificationFragment(), "Spec");
        myAdapter.addFragments(new ProjectCommunicationFragment(), "Comm");
        mViewPager.setAdapter(myAdapter);
    }

    private void setupTabIcons(){
        Log.d(TAG, "setupTabIcons: ");
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_project_detail, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
