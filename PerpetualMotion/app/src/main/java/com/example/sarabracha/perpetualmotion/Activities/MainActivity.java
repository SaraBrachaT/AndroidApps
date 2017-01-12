package com.example.sarabracha.perpetualmotion.Activities;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.sarabracha.perpetualmotion.R;
import com.example.sarabracha.perpetualmotion.game.Card;
import com.example.sarabracha.perpetualmotion.game.Game;
import com.example.sarabracha.perpetualmotion.lib.CardPilesAdapter;
import com.google.gson.Gson;

import java.util.EmptyStackException;

public class MainActivity extends AppCompatActivity {

    private Game mCurrentGame;
    private CardPilesAdapter mAdapter;
    private TextView mTv_cardsRemaining, mTv_cardsInDeck;
    private View mSbContainer;
    private boolean[] mCheckedPiles;
    private String mPrefs = "PREFS", mKeyAutoSave, mKeyShowErrors,
            mKeyCheckedPiles = "CHECKED_PILES", mKeyGame = "GAME", mKeyGameOver = "GAME_OVER";
    private String mWINNER_MSG, mNON_WINNER_MSG;
    private boolean mPrefUseAutoSave, mPrefShowErrors, mGameOver;

    @Override protected void onSaveInstanceState (Bundle outState)
    {
        super.onSaveInstanceState (outState);
        outState.putBooleanArray (mKeyCheckedPiles, mCheckedPiles);
        outState.putString (mKeyGame, getJSONof (mCurrentGame));
        outState.putBoolean (mKeyGameOver, mGameOver);
    }

    @Override protected void onStop ()
    {
        saveToSharedPref ();
        super.onStop ();
    }

    private void saveToSharedPref ()
    {
        // Create a SP object that (creates if needed and) uses the value of mPrefs as the file name
        SharedPreferences preferences = getSharedPreferences (mPrefs, MODE_PRIVATE);

        // Create an Editor object to write changes to the preferences object above
        SharedPreferences.Editor editor = preferences.edit ();

        // clear whatever was set last time
        editor.clear ();

        // save "show/hide turn errors" preference
        editor.putBoolean (mKeyShowErrors, mPrefShowErrors);

        // save "autoSave" preference
        editor.putBoolean (mKeyAutoSave, mPrefUseAutoSave);

        // if autoSave is on then save the board
        saveBoardToSharedPrefsIfAutoSaveIsOn (editor);

        // apply the changes to the XML file in the device's storage
        editor.apply ();
    }


    private void saveBoardToSharedPrefsIfAutoSaveIsOn (SharedPreferences.Editor editor)
    {
        if (mPrefUseAutoSave) {

            // Game Object
            editor.putString (mKeyGame, getJSONof (mCurrentGame));

            // Game Over Status
            editor.putBoolean (mKeyGameOver, mGameOver);

            // Checks currently checked/unchecked in RecyclerView
            for (int i = 0; i < mCheckedPiles.length; i++) {
                editor.putBoolean (mKeyCheckedPiles + i, mCheckedPiles[i]);
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setValuesFromResources ();
        setDefaultValuesForPreferences ();
        setupGUI();
        setupBoard();
        restoreAllPrefs ();
        doInitialStartGame(savedInstanceState);
        setupFAB();

    }

    private void setValuesFromResources ()
    {
        mKeyAutoSave = getString (R.string.key_use_auto_save);
        mKeyShowErrors = getString (R.string.key_show_turn_specific_error_messages);
        mWINNER_MSG = getString (R.string.you_have_cleared_the_board)
                .concat ("\n").concat (getString (R.string.new_game_question));
        mNON_WINNER_MSG = getString (R.string.no_more_turns_remain)
                .concat ("\n").concat (getString (R.string.new_game_question));
    }

    private void setupGUI() {
        mSbContainer = findViewById(R.id.cl_main);
        mTv_cardsRemaining = (TextView) findViewById(R.id.tv_cards_remaining_to_discard);
        mTv_cardsInDeck = (TextView) findViewById(R.id.tv_cards_in_deck);
    }

    private void setupBoard() {
        mCheckedPiles = new boolean[] {false, false, false, false};//creating array of check boxes so can restore if rotated
        mAdapter = new CardPilesAdapter(mCheckedPiles, getString(R.string.cards_in_stack));
        mAdapter.setOnItemClickListener (listener);

        //TODO: set Listener
        //creating grid
        RecyclerView piles = (RecyclerView) findViewById(R.id.rv_piles);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this,getResources().getInteger(R.integer.rv_columns));

        layoutManager.setAutoMeasureEnabled(true);

        piles.setHasFixedSize(true);
        piles.setLayoutManager(layoutManager);
        piles.setAdapter(mAdapter);
    }

    private void restoreAllPrefs ()
    {
        // Since this is for reading only, no editor is needed unlike in saveRestoreState
        SharedPreferences preferences = getSharedPreferences (mPrefs, MODE_PRIVATE);

        // restore AutoSave preference value
        mPrefUseAutoSave = preferences.getBoolean (mKeyAutoSave, true);
        mPrefShowErrors = preferences.getBoolean (mKeyShowErrors, true);
    }


    private void doInitialStartGame(Bundle savedInstanceState) {
        // If this is NOT the first run, meaning, we're recreating as a result of a device rotation
        // restore the board (meaning both cards and user's checks) as from before the rotation
        if (savedInstanceState != null) {
            restoreSavedGameFromBundle (savedInstanceState);
        }
        // Otherwise, if this is a fresh start of the Activity and NOT after a rotation
        else {
            startNewGameOrFromPrefs ();
        }
    }

    private void startNewGameOrFromPrefs ()
    {
        if (mPrefUseAutoSave && isValidGameInPrefs ()) {
            restoreSavedGameFromPrefs ();
        }
        else {
            startNewGame ();
        }
    }
    private boolean isValidGameInPrefs ()
    {
        SharedPreferences preferences = getSharedPreferences (mPrefs, MODE_PRIVATE);

        // restore the current game
        String savedGame = preferences.getString (mKeyGame, "");
        return (!savedGame.equals (""));
    }

    private void restoreSavedGameFromPrefs ()
    {
        SharedPreferences preferences = getSharedPreferences (mPrefs, MODE_PRIVATE);

        // restore the current game
        String savedGame = preferences.getString (mKeyGame, "");

        mCurrentGame = restoreGameFromJSON (savedGame);
        mGameOver = preferences.getBoolean (mKeyGameOver, true);

        // update the board to reflect this game's top cards and then, as usual, clear checks
        doPostTurnUpdates ();

        // Finally, update the checks to reflect the user's selection last time
        for (int i = 0; i < mCheckedPiles.length; i++) {
            mCheckedPiles[i] = preferences.getBoolean (mKeyCheckedPiles + i, false);
        }
        mAdapter.overwriteChecksFrom (mCheckedPiles);
    }


    private void restoreSavedGameFromBundle (Bundle savedInstanceState)
    {
        // restore the current game
        mCurrentGame = restoreGameFromJSON (savedInstanceState.getString (mKeyGame));
        mGameOver = savedInstanceState.getBoolean (mKeyGameOver);

        // update the board to reflect this game's top cards and then, as usual, clear checks
        doPostTurnUpdates ();

        // Finally, update the checks to reflect the user's selection before rotation
        mAdapter.overwriteChecksFrom (savedInstanceState.getBooleanArray (mKeyCheckedPiles));
    }

    private void startNewGame ()
    {
        // set game over to false
        mGameOver = false;

        // create an instance of our Java IDGame class, which also takes the first turn
        mCurrentGame = new Game ();

        // Update the GUI
        doPostTurnUpdates ();

        // Show New Game message
        Snackbar.make (mSbContainer, R.string.welcome_new_game, Snackbar.LENGTH_SHORT).show ();
    }

    private void startGame() {

    }

    private void setupFAB()     {
        // Display the rules (from Java) when the user clicks on the Floating Action Button
        FloatingActionButton fab = (FloatingActionButton) findViewById (R.id.fab);
        fab.setOnClickListener (new View.OnClickListener ()
        {
            @Override
            public void onClick (View view)
            {
                showInfoDialog ("Information", mCurrentGame.getRules ());
            }
        });
    }

    /**
     * In case this is the first time the user has ever run the app
     * Set the default choices (specified in the prefs xml file)
     * for the three settings we have here in this application that are in that xml file:
     * Use AutoSave, Play Against Computer and Computer Starts First
     */

    private void setDefaultValuesForPreferences ()
    {
        PreferenceManager.setDefaultValues (getApplicationContext (), R.xml.main_prefs, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override public boolean onPrepareOptionsMenu (Menu menu)
    {
        menu.findItem (R.id.action_toggle_auto_save).setChecked (mPrefUseAutoSave);
        menu.findItem (R.id.action_turn_show_error_messages).setChecked (mPrefShowErrors);
        return super.onPrepareOptionsMenu (menu);
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        // TODO Add settings to XML and handle it here
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId ()) {

            //noinspection SimplifiableIfStatement
            case R.id.action_about: {
                showAbout ();
                return true;
            }
            case R.id.action_toggle_auto_save: {
                toggleMenuItem (item);
                mPrefUseAutoSave = item.isChecked ();
                return true;
            }
            case R.id.action_turn_show_error_messages:
                toggleMenuItem (item);
                mPrefShowErrors = item.isChecked ();
                return true;
            default:
                return super.onOptionsItemSelected (item);

        }
    }

    private void toggleMenuItem (MenuItem item)
    {
        item.setChecked (!item.isChecked ());
    }


    private void doPostTurnUpdates() {
        updateStatusBar ();
        updateRecyclerViewAdapter ();
        checkForGameOver ();
    }
    private void updateStatusBar ()
    {
        // Update the Status Bar with the number of cards left (from Java) via our current game obj
        mTv_cardsRemaining.setText (getString (R.string.cards_to_discard).concat
                (String.valueOf (mCurrentGame.getNumCardsLeftToDiscard ())));

        mTv_cardsInDeck.setText (getString (R.string.in_deck).concat (
                String.valueOf (mCurrentGame.getNumberOfCardsLeftInDeck ())));
    }

    private void updateRecyclerViewAdapter ()
    {
        // get the data for the new board from our game object (Java) which tracks the four stacks
        Card[] currentTops = mCurrentGame.getCurrentStacksTopIncludingNulls ();

        // temporary card used when updating the board below
        Card currentCard;

        // Update the board one pile/card at a time
        for (int i = 0; i < currentTops.length; i++) {
            currentCard = currentTops[i];

            // Have Adapter set each card to the matching top card of each stack
            mAdapter.updatePile (i, currentCard,
                    mCurrentGame.getNumberOfCardsInStackAtPosition (i));

            // Clear any checks that the user might have just set
            mAdapter.clearCheck (i);
        }
    }

    /**
     * If the game is over, this method outputs a dialog box with the correct message (win/not)
     */
    private void checkForGameOver ()
    {
        // If the game is over, let the user know what happened and then start a new game
        if (mCurrentGame.gameWon ()) {
            mGameOver = true;
            showGameOverDialog (getString (R.string.game_over),
                    mCurrentGame.isWinner () ? mWINNER_MSG : mNON_WINNER_MSG);
        }
    }

    /**
     * This method lets us know how many cards have been checked off
     *
     * @param checkedPiles the array of checked Cards
     * @return the number of cards checked
     */
    private int getCountOfChecks (boolean[] checkedPiles)
    {
        int totalChecked = 0;
        for (boolean checkedPile : checkedPiles) {
            totalChecked += checkedPile ? 1 : 0;
        }
        return totalChecked;
    }

    /**
     * This method lets us know what the n checked pile contains (out of only checked piles, not 4)
     *
     * @param checkedPiles 4-element boolean array of four checked/unchecked piles
     * @param position     The checked pile number (0 through x) of the checked pile
     * @return The pile number (out of the checked piles, not out of all 4 piles)
     */
    private int getCheckedItem (boolean[] checkedPiles, int position)
    {
        // create a new int array containing the number of elements == the number of checked cards
        int[] checkedItems = new int[getCountOfChecks (checkedPiles)];

        // i is the index for the 4-element array of all stacks passed in
        // j is the index for the new array of position numbers just created
        for (int i = 0, j = 0; i < checkedPiles.length; i++) {
            // increment j only if current element is true
            if (checkedPiles[i]) {
                checkedItems[j++] = i;
            }

        }
        return checkedItems[position];
    }

    private void showSB (String msg)
    {
        if (mPrefShowErrors) {
            Snackbar.make (mSbContainer, msg, Snackbar.LENGTH_LONG).show ();
        }
    }

    private void showSB_AlreadyGameOver ()
    {
        if (mPrefShowErrors) {
            Snackbar.make (mSbContainer, "Game has already ended", Snackbar.LENGTH_LONG)
                    .setAction (R.string.new_game, new View.OnClickListener ()
                    {
                        @Override public void onClick (View view)
                        {
                            startNewGame ();
                        }
                    }).show ();
        }
    }

    private void showSBErrorDiscardOneTwoOrOther (int checkedCount)
    {
        if (mPrefShowErrors) {
            String errorMsg = getString (checkedCount > 2 || checkedCount < 1 ?
                    R.string.turn_error_discard_other :
                    checkedCount == 2 ? R.string.turn_error_discard_two
                            : R.string.turn_error_discard_one);

            Snackbar.make (mSbContainer, errorMsg, Snackbar.LENGTH_LONG).show ();
        }
    }

    /**
     * This method handles the user's choice to discard either:
     * the lower of two cards of the same suit or two cards of the same rank
     *
     * @param view this is the calling Object (e.g. button): irrelevant to us so we ignore it
     */
    public void turn_action_discard (@SuppressWarnings ("UnusedParameters") View view)
    {
        if (mGameOver) {
            showSB_AlreadyGameOver ();
        }
        else {
            boolean[] checkedPiles = mAdapter.getCheckedPiles ();
            attemptDiscard (checkedPiles, getCountOfChecks (checkedPiles));
        }
    }

    private void attemptDiscard (boolean[] checkedPiles, int countOfChecks)
    {
        try {
            discardOneOrTwo (checkedPiles, countOfChecks);
            doPostTurnUpdates ();
        }
        catch (EmptyStackException ese) {
            showSB ("Cannot Discard from an empty pile/stack");
        }
        catch (UnsupportedOperationException uoe) {
            showSB (uoe.getMessage ());
        }
    }

    private void discardOneOrTwo (boolean[] checkedPiles, int countOfChecks)
    {
        int pileTopToDiscard, secondPileTopToDiscard;
        switch (countOfChecks) {
            case 2: {
                pileTopToDiscard = getCheckedItem (checkedPiles, 0);
                secondPileTopToDiscard = getCheckedItem (checkedPiles, 1);
                mCurrentGame.discard (pileTopToDiscard, secondPileTopToDiscard);
                break;
            }
            default: {
                showSBErrorDiscardOneTwoOrOther (countOfChecks);
            }
        }
    }

    /**
     * This method handles the user's choice to deal one card to each stack (i.e. new top of each)
     *
     * @param view this is the calling Object (e.g. button): irrelevant to us so we ignore it
     */
    public void turn_action_deal (@SuppressWarnings ("UnusedParameters") View view)
    {
        if (mGameOver) {
            showSB_AlreadyGameOver ();
        }
        else {
            try {
                mCurrentGame.clickDeal ();
            }
            catch (EmptyStackException ese) {
                showInfoDialog (R.string.title_no_cards_remain,
                        R.string.body_all_cards_dealt_to_stacks);
            }

            // cards will remain as above but clear checkboxes either way, even if deck is empty
            doPostTurnUpdates ();
        }
    }

    /**
     * Starts a new game when ActionBar button is pressed
     *
     * @param item MenuItem that triggered this call - not relevant to us so it is ignored
     */
    public void startNewGame (@SuppressWarnings ("UnusedParameters") MenuItem item)
    {
        startNewGame ();
    }

    public void undoLastMove (@SuppressWarnings ("UnusedParameters") MenuItem item)
    {
        undoLastMove ();
    }

    private void showAbout ()
    {
        showInfoDialog (R.string.app_name, R.string.about_message);
    }

    private void undoLastMove ()
    {
        try {
            mCurrentGame.undoLatestTurn ();
            mGameOver = false;
            doPostTurnUpdates ();
        }
        catch (UnsupportedOperationException uoe) {
            showInfoDialog ("Can't Undo", uoe.getMessage ());
        }
    }

    /**
     * Shows an Android (nicer) equivalent to JOptionPane
     *
     * @param strTitle Title of the Dialog box
     * @param strMsg   Message (body) of the Dialog box
     */
    private void showGameOverDialog (String strTitle, String strMsg)
    {
// create the listener for the dialog
        final DialogInterface.OnClickListener doNothingListener =
                new DialogInterface.OnClickListener ()
                {
                    @Override
                    public void onClick (DialogInterface dialog, int which)
                    {
                        //nothing needed to do here
                    }
                };

// create the listener for the dialog
        final DialogInterface.OnClickListener newGameListener =
                new DialogInterface.OnClickListener ()
                {
                    @Override
                    public void onClick (DialogInterface dialog, int which)
                    {
                        startNewGame ();
                    }
                };


        // Create the AlertDialog.Builder object
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder (MainActivity.this);

        // Use the AlertDialog's Builder Class methods to set the title, icon, message, et al.
        // These could all be chained as one long statement, if desired
        alertDialogBuilder.setTitle (strTitle);
        alertDialogBuilder.setIcon (R.drawable.ic_action_info);
        alertDialogBuilder.setMessage (strMsg);
        alertDialogBuilder.setCancelable (true);
        alertDialogBuilder.setNegativeButton (getString (R.string.No), doNothingListener);
        alertDialogBuilder.setPositiveButton (getString (R.string.Yes), newGameListener);

        // Create and Show the Dialog
        alertDialogBuilder.show ();
    }

    /**
     * Overloaded XML version of showGameOverDialog(String, String) method
     *
     * @param titleID Title stored in XML resource (e.g. strings.xml)
     * @param msgID   Message (body) stored in XML resource (e.g. strings.xml)
     */
    private void showGameOverDialog (int titleID, int msgID)
    {
        showGameOverDialog (getString (titleID), getString (msgID));
    }

    /**
     * Shows an Android (nicer) equivalent to JOptionPane
     *
     * @param strTitle Title of the Dialog box
     * @param strMsg   Message (body) of the Dialog box
     */
    private void showInfoDialog (String strTitle, String strMsg)
    {
// create the listener for the dialog
        final DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener ()
        {
            @Override
            public void onClick (DialogInterface dialog, int which)
            {
                //nothing needed to do here
            }
        };

        // Create the AlertDialog.Builder object
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder (MainActivity.this);

        // Use the AlertDialog's Builder Class methods to set the title, icon, message, et al.
        // These could all be chained as one long statement, if desired
        alertDialogBuilder.setTitle (strTitle);
        alertDialogBuilder.setIcon (R.drawable.ic_action_info);
        alertDialogBuilder.setMessage (strMsg);
        alertDialogBuilder.setCancelable (true);
        alertDialogBuilder.setNeutralButton (getString (R.string.OK), listener);

        // Create and Show the Dialog
        alertDialogBuilder.show ();
    }

    /**
     * Overloaded XML version of showInfoDialog(String, String) method
     *
     * @param titleID Title stored in XML resource (e.g. strings.xml)
     * @param msgID   Message (body) stored in XML resource (e.g. strings.xml)
     */
    private void showInfoDialog (int titleID, int msgID)
    {
        showInfoDialog (getString (titleID), getString (msgID));
    }

    /**
     * Serializes the current IDGame object so it can be stored in the Bundle during rotation
     *
     * @param obj the game
     * @return Serialized (String) of the current game object
     */
    private String getJSONof (Game obj)
    {
        Gson gson = new Gson ();
        return gson.toJson (obj);
    }

    /**
     * Reverses the serialization of the IDGame object String back to an IDGame object
     *
     * @param json The serialized String of the game object
     * @return The game object
     */
    private Game restoreGameFromJSON (String json)
    {
        Gson gson = new Gson ();
        return gson.fromJson (json, Game.class);
    }

    // LG work-around for select older devices
    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event)
    {
        boolean isOldLG = ((keyCode == KeyEvent.KEYCODE_MENU) &&
                (Build.VERSION.SDK_INT <= 16) &&
                (Build.MANUFACTURER.compareTo ("LGE") == 0));

        //noinspection SimplifiableConditionalExpression
        return isOldLG ? true : super.onKeyDown (keyCode, event);
    }

    @Override
    public boolean onKeyUp (int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_MENU) &&
                (Build.VERSION.SDK_INT <= 16) &&
                (Build.MANUFACTURER.compareTo ("LGE") == 0)) {
            openOptionsMenu ();
            return true;
        }
        return super.onKeyUp (keyCode, event);
    }

    /**
     * This object of our custom Listener class handles events in the adapter; created here anon.
     * This is called from the Adapter when the user clicks on the board.
     * This leaves the Adapter to handle only the Model part of MVC, not View or Controller
     */
    private final CardPilesAdapter.OIClickListener
            listener = new CardPilesAdapter.OIClickListener ()
    {
        public void onItemClick (int position, View view)
        {
            try {
                if (mCurrentGame.getNumberOfCardsInStackAtPosition (position) > 0) {
                    if (mGameOver) {
                        showSB_AlreadyGameOver ();
                    }
                    else {
                        mAdapter.toggleCheck (position);
                    }
                }
                // otherwise, if this stack is empty (and no card shown), then ignore the click
            }
            catch (Exception e) {
                Log.d ("STACK", "Toggle Crashed: " + e.getMessage ());
                // No reason for it to crash but since it did...
            }
        }

    };
}
