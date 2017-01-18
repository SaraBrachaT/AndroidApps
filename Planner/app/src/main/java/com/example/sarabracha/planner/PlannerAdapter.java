package com.example.sarabracha.planner;


import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DecimalFormat;


public class PlannerAdapter extends RecyclerView.Adapter<PlannerAdapter.ViewHolder> {

    private String mPeriodSlots[], mData[];
    private int mHoursInDay, mHourSubPeriods, mStartHour, mTotalPeriods;
    private int mAll24Hours[], mOneSetOfHourSubPeriods[];
    private String mAMPM[];

    public PlannerAdapter(int startHour, int hoursInDay, int hourSubPeriods, String[] incomingList) {
        validateData(startHour, hoursInDay, hourSubPeriods);
        initializeArrays(startHour, hoursInDay, hourSubPeriods);
        createArrays();
        fillArrays(incomingList);
    }

    private void validateData(int startHour, int hoursInDay, int hourSubPeriods) {
        validateStartHour(startHour);
        validateHoursInDay(startHour, hoursInDay);
        validateHourSubPeriods(hourSubPeriods);
    }

    private void validateStartHour(int startHour) {
        if (startHour < 0 || startHour > 23) {
            throw new IllegalArgumentException("Start Hour must be between 0 and 23 inclusive");
        }
    }

    private void validateHoursInDay(int startHour, int hoursInDay) {
        if (hoursInDay < 1 || hoursInDay > 24 || hoursInDay + startHour > 24) {
            throw new IllegalArgumentException("Hours in Day must be between 1 and 24 inclusive" +
                    "AND must end by midnight. " +
                    "The later the Start Time, the fewer available hours in the day.");
        }
    }

    private void validateHourSubPeriods(int hourSubPeriods) {
        if (hourSubPeriods < 1 || hourSubPeriods > 60 || 60 % hourSubPeriods != 0) {
            throw new IllegalArgumentException("Hour sub-periods must be:" +
                    " between 1 and 6o inclusive and " +
                    "evenly divisible into 60");
        }
    }

    private void initializeArrays(int startHour, int hoursInDay, int hourSubPeriods) {
        mStartHour = startHour;
        mHoursInDay = hoursInDay;
        mHourSubPeriods = hourSubPeriods;
        mTotalPeriods = mHoursInDay * mHourSubPeriods;
    }

    private void createArrays() {
        mAll24Hours = new int[24];
        mAMPM = new String[24];

        mOneSetOfHourSubPeriods = new int[mHourSubPeriods];

        mPeriodSlots = new String[mTotalPeriods];
        mData = new String[mTotalPeriods];
    }

    private void fillArrays(String[] incomingData) {
        fill24HoursAndAMPM();
        fillPeriodBaseSlots();
        fillScheduleSlots();
        fillSchedule(incomingData);
    }

    private void fillSchedule(String[] incomingData) {
        System.arraycopy(incomingData, 0, mData, 0, mData.length);
    }

    private void fill24HoursAndAMPM() {
        fillAll12AMHours();
        fillAll12PMHours();
    }

    private void fillAll12AMHours() {
        mAll24Hours[0] = 12;
        mAMPM[0] = "AM";
        for (int i = 1; i < 12; i++) {
            mAll24Hours[i] = i;
            mAMPM[i] = "AM";
        }
    }

    private void fillAll12PMHours() {
        mAll24Hours[12] = 12;
        mAMPM[12] = "PM";

        for (int i = 13; i < 24; i++) {
            mAll24Hours[i] = i - 12;
            mAMPM[i] = "PM";
        }
    }

    private void fillPeriodBaseSlots() {
        int len = mOneSetOfHourSubPeriods.length;

        for (int i = 0; i < len; i++) {
            mOneSetOfHourSubPeriods[i] = (60 / mHourSubPeriods * i);
        }
    }

    private void fillScheduleSlots() {
        int hour;
        int len = mOneSetOfHourSubPeriods.length;

        String AMPM, period = "00";
        DecimalFormat df = new DecimalFormat("00");

        for (int slot = 0, i = 0;
             slot < mPeriodSlots.length;
             slot += len, i++) {
            {
                hour = mAll24Hours[i + mStartHour];
                AMPM = mAMPM[i + mStartHour];

                for (int periodSlot = 0; periodSlot < len; periodSlot++) {

                    period = df.format(mOneSetOfHourSubPeriods[periodSlot]);

                    mPeriodSlots[slot + periodSlot] = hour + ":" + period + ' ' + AMPM;

                    //mData[slot + periodSlot] = "test " + periodSlot;

                    Log.d("RV", "Slot " + (slot + periodSlot) + "; Hour: " + hour +
                            "; Period: " + period + "; AMPM: " + AMPM);
                }
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.rv_planner_slot_item, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TextView tv_slot_time = holder.tv_slot_time;
        TextView tv_task = holder.tv_task;

        tv_slot_time.setText(mPeriodSlots[position]);
        tv_task.setText(mData[position]);
        tv_task.setTag(position);
    }

    /**
     * Returns how many items this adapter manages
     *
     * @return number of items
     */
    @Override
    public int getItemCount() {
        return mData.length;
    }

    /**
     * Gets the number of active/working hours in the schedule
     *
     * @return number of hours
     */
    public int getHoursInDay() {
        return mHoursInDay;
    }

    /**
     * Gets the number of divisions per hour used (i.e. 4 for 15 minutes, 2 for half-hour, et al.)
     *
     * @return number of periods per hour
     */
    public int getHourSubPeriods() {
        return mHourSubPeriods;
    }

    /**
     * Gets the zero-based hour at which the user's schedule starts
     *
     * @return starting hour for schedule
     */
    public int getStartHour() {
        return mStartHour;
    }

    /**
     * Returns the total number of periods in the user's schedule
     *
     * @return
     */
    public int getTotalPeriods() {
        return mTotalPeriods;
    }

    public void changeTimeSlotEvent(int position, String event) {
        mData[position] = event;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tv_slot_time, tv_task;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            tv_slot_time = (TextView) itemLayoutView.findViewById(R.id.tv_slot_time);
            tv_task = (TextView) itemLayoutView.findViewById(R.id.tv_task);
        }
    }
}

