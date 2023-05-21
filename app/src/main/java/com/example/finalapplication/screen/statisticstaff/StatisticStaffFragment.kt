package com.example.finalapplication.screen.statisticstaff

import android.app.DatePickerDialog
import android.graphics.Color
import android.graphics.Typeface
import android.icu.util.Calendar
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.finalapplication.R
import com.example.finalapplication.data.model.Post
import com.example.finalapplication.databinding.FragmentStatisticBinding
import com.example.finalapplication.utils.TimeConstant
import com.example.finalapplication.utils.base.BaseFragment
import com.example.finalapplication.utils.toDateTime
import com.example.finalapplication.utils.toDay
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.google.firebase.firestore.core.EventManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Timer
import java.util.logging.Handler
import kotlin.concurrent.schedule

class StatisticStaffFragment :
    BaseFragment<FragmentStatisticBinding>(FragmentStatisticBinding::inflate) {
    private val viewModel by viewModel<StatisticStaffViewModel>()
    private val roomAllHires = mutableListOf<Post>()
    private val roomVerifiedHire = mutableListOf<Post>()
    private val timeRoom = mutableListOf<String>()
    private val countRoom = mutableListOf<Float>()
    private val revenueRoom = mutableListOf<Long>()
    private val copyRevenueRoom = mutableListOf<Long>()
    private val copyTimeRoom = mutableListOf<String>()
    private val timeRoomVerified = mutableListOf<String>()
    private val countRoomVerified = mutableListOf<Float>()
    private val copyTimeRoomVerified = mutableListOf<String>()
    private var numberAllHired = 0f
    private var numberAllNotHired = 0f
    private var numberAllNotHired2 = 0f
    private var numberVerifiedHired = 0f
    private var numberVerifiedNotHired = 0f
    private var numberVerifiedNotHired2 = 0f
    private var revenue = 0f
    private var mYear = 0
    private var mMonth: Int = 0
    private var mDay: Int = 0
    private var startTime: Long = 0;
    private var endTime: Long = 0;

    override fun bindData() {
        viewModel.apply {
            roomAllHired.observe(this@StatisticStaffFragment) { data ->
                var number: Int = data.size
                numberAllHired = number.toFloat()
                Log.d("numberHired", "bindData: $numberAllHired")
                roomAllHires.clear()
                roomAllHires.addAll(data)
            }
            roomAllNotHired.observe(this@StatisticStaffFragment) { data ->
                var number: Int = data.size
                numberAllNotHired = number.toFloat()
                Log.d("numberHired", "bindData: $numberAllNotHired")
            }
            roomAllNotHired2.observe(this@StatisticStaffFragment) { data ->
                var number: Int = data.size
                numberAllNotHired2 = number.toFloat()
                Log.d("numberHired", "bindData: $numberAllNotHired")
            }
            roomVerifiedHired.observe(this@StatisticStaffFragment) { data ->
                var number: Int = data.size
                numberVerifiedHired = number.toFloat()
                Log.d("numberHired", "verifiedData: $numberVerifiedHired")
                roomVerifiedHire.clear()
                roomVerifiedHire.addAll(data)
            }
            roomVerifiedNotHired.observe(this@StatisticStaffFragment) { data ->
                var number: Int = data.size
                numberVerifiedNotHired = number.toFloat()
                Log.d("numberHired", "verifiedData: $numberVerifiedNotHired")
            }
            roomVerifiedNotHired2.observe(this@StatisticStaffFragment) { data ->
                var number: Int = data.size
                numberVerifiedNotHired2 = number.toFloat()
                Log.d("numberHired", "verifiedData: $numberVerifiedNotHired")
            }
        }
    }

    fun countHiredAll(start: Long, end: Long) {
        var cnt = 0
        copyTimeRoom.clear()
        timeRoom.clear()
        countRoom.clear()

        for (i in 0 until roomAllHires.size) {
            var time = roomAllHires[i].timeHired
            timeRoom.add(time.toDay())
        }

        Log.d("abc", "size: " + timeRoom.size)
        if (!timeRoom.isEmpty()) {
            copyTimeRoom.add(timeRoom[0])
            countRoom.add(1f)

            for (i in 1 until timeRoom.size) {
                if (timeRoom[i] == copyTimeRoom[cnt]) {
                    countRoom[cnt] = (countRoom[cnt] + 1f);

                } else {
                    cnt++
                    copyTimeRoom.add(timeRoom[i])
                    countRoom.add(1f)
                }
            }
        }

    }

    fun countHireVerified(start: Long, end: Long) {
        var cnt = 0
        copyTimeRoomVerified.clear()
        timeRoomVerified.clear()
        countRoomVerified.clear()
        revenueRoom.clear()
        copyRevenueRoom.clear()
        for (i in 0 until roomVerifiedHire.size) {
            var time = roomVerifiedHire[i].timeHired
            timeRoomVerified.add(time.toDay())
            revenueRoom.add((roomVerifiedHire[i].price * 0.1).toLong())
        }
        Log.d("HireVerified", "size: " + timeRoomVerified.size)
        if (!timeRoomVerified.isEmpty()) {
            copyTimeRoomVerified.add(timeRoomVerified[0])
            countRoomVerified.add(1f)
            copyRevenueRoom.add(revenueRoom[0])
            for (i in 1 until timeRoomVerified.size) {
                if (timeRoomVerified[i] == copyTimeRoomVerified[cnt]) {
                    countRoomVerified[cnt] = countRoomVerified[cnt] + 1f
                    copyRevenueRoom[cnt] = copyRevenueRoom[cnt] + revenueRoom[i]
                } else {
                    cnt++
                    copyTimeRoomVerified.add(timeRoomVerified[i])
                    countRoomVerified.add(1f)
                    copyRevenueRoom.add(revenueRoom[i])
                }
            }
        }

    }


    override fun handleEvent() {
        binding.btnStart.setOnClickListener { setTime(0) }
        binding.btnEnd.setOnClickListener { setTime(1) }
        binding.find.setOnClickListener {
            Log.d("find", "handleEvent: " + startTime + " " + endTime)
            viewModel.statistic(startTime, endTime)
            lifecycleScope.launch {
                delay(TimeConstant.DELAY_DATA)
                countHiredAll(startTime, endTime)
                countHireVerified(startTime, endTime)
                pieChartAll()
                barChartAll()
                pieChartVerified()
                barChartVerified()
                barChartRevenue()
            }
        }
    }

    override fun onResume() {
        bindData()
        handleEvent()
        super.onResume()
    }

    override fun initData() {
        viewModel.statistic(0, System.currentTimeMillis())
        lifecycleScope.launch {
            delay(TimeConstant.DELAY_DATA)
            countHiredAll(0, System.currentTimeMillis())
            countHireVerified(0, System.currentTimeMillis())
            Log.d("tag", "initData time: " + System.currentTimeMillis())
            Log.d("tag", "initData:verify " + numberVerifiedNotHired)
            pieChartAll()
            barChartAll()
            pieChartVerified()
            barChartVerified()
            barChartRevenue()
        }

    }

    fun setTime(time: Int) {
        var calendar = Calendar.getInstance()
        mYear = calendar.get(Calendar.YEAR)
        mMonth = calendar.get(Calendar.MONTH)
        mDay = calendar.get(Calendar.DAY_OF_MONTH)
        var datePickerDialog =
            DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    var calendar2 = Calendar.getInstance()
                    calendar2.set(year, (monthOfYear), dayOfMonth)
                    calendar2.set(Calendar.MILLISECOND, 0)
                    calendar2.set(Calendar.SECOND, 0)
                    calendar2.set(Calendar.MINUTE, 0)
                    calendar2.set(Calendar.HOUR_OF_DAY, 0)
                    if (time == 0) {
                        binding.start.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)
                        startTime = calendar2.timeInMillis
                    } else {
                        binding.end.setText("" + dayOfMonth + "/" + (monthOfYear + 1) + "/" + year)
                        calendar2.set(year, (monthOfYear), dayOfMonth + 1)
                        endTime = calendar2.timeInMillis
                    }
                }, mYear, mMonth, mDay
            )
        datePickerDialog.show()
    }

    fun pieChartAll() {
        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.getDescription().setEnabled(false)
        binding.pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        // on below line we are setting drag for our pie chart
        binding.pieChart.setDragDecelerationFrictionCoef(0.95f)

        // on below line we are setting hole
        // and hole color for pie chart
        binding.pieChart.setDrawHoleEnabled(true)
        binding.pieChart.setHoleColor(Color.WHITE)

        // on below line we are setting circle color and alpha
        binding.pieChart.setTransparentCircleColor(Color.WHITE)
        binding.pieChart.setTransparentCircleAlpha(110)

        // on  below line we are setting hole radius
        binding.pieChart.setHoleRadius(58f)
        binding.pieChart.setTransparentCircleRadius(61f)

        // on below line we are setting center text
        binding.pieChart.setDrawCenterText(true)

        // on below line we are setting
        // rotation for our pie chart
        binding.pieChart.setRotationAngle(0f)

        // enable rotation of the pieChart by touch
        binding.pieChart.setRotationEnabled(true)
        binding.pieChart.setHighlightPerTapEnabled(true)

        // on below line we are setting animation for our pie chart
        binding.pieChart.animateY(1400, Easing.EaseInOutQuad)

        // on below line we are disabling our legend for pie chart
        binding.pieChart.setEntryLabelColor(Color.WHITE)
        binding.pieChart.setEntryLabelTextSize(12f)
        Log.d("all hired", "pieChartAll: " + numberAllNotHired)
        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(numberAllHired, "Đã Thuê"))
        entries.add(PieEntry(numberAllNotHired + numberAllNotHired2, "Chưa Thuê"))

        // on below line we are setting pie data set
        val dataSet = PieDataSet(entries, "Phòng thuê")

        // on below line we are setting icons.
        dataSet.setDrawIcons(true)

        // on below line we are setting slice for pie
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors to list
        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.piechar2))
        colors.add(resources.getColor(R.color.piechar3))

        // on below line we are setting colors.
        dataSet.colors = colors

        // on below line we are setting pie data set
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        binding.pieChart.setData(data)

        // undo all highlights
        binding.pieChart.highlightValues(null)
        binding.pieChart.description.text = "Phòng Thuê"
        binding.pieChart.centerText = "Phòng Thuê"
        // loading chart
        binding.pieChart.invalidate()
    }

    fun pieChartVerified() {
        binding.pieChart2.setUsePercentValues(true)
        binding.pieChart2.getDescription().setEnabled(false)
        binding.pieChart2.setExtraOffsets(5f, 10f, 5f, 5f)

        // on below line we are setting drag for our pie chart
        binding.pieChart2.setDragDecelerationFrictionCoef(0.95f)

        // on below line we are setting hole
        // and hole color for pie chart
        binding.pieChart2.setDrawHoleEnabled(true)
        binding.pieChart2.setHoleColor(Color.WHITE)

        // on below line we are setting circle color and alpha
        binding.pieChart2.setTransparentCircleColor(Color.WHITE)
        binding.pieChart2.setTransparentCircleAlpha(110)

        // on  below line we are setting hole radius
        binding.pieChart2.setHoleRadius(58f)
        binding.pieChart.setTransparentCircleRadius(61f)

        // on below line we are setting center text
        binding.pieChart2.setDrawCenterText(true)

        // on below line we are setting
        // rotation for our pie chart
        binding.pieChart2.setRotationAngle(0f)

        // enable rotation of the pieChart by touch
        binding.pieChart2.setRotationEnabled(true)
        binding.pieChart.setHighlightPerTapEnabled(true)

        // on below line we are setting animation for our pie chart
        binding.pieChart2.animateY(1400, Easing.EaseInOutQuad)
        binding.pieChart2.setEntryLabelColor(Color.WHITE)
        binding.pieChart2.setEntryLabelTextSize(12f)

        // on below line we are creating array list and
        // adding data to it to display in pie chart
        val entries: ArrayList<PieEntry> = ArrayList()
        // rooms.add(PieEntry(numberAllHired, "Đã Thuê"))
//        rooms.add(PieEntry(numberAllNotHired, "Chưa Thuê"))
        entries.add(PieEntry(numberVerifiedHired, "Đã Thuê"))
        entries.add(PieEntry(numberVerifiedNotHired + numberVerifiedNotHired2, "Chưa Thuê"))

        // on below line we are setting pie data set
        val dataSet = PieDataSet(entries, "Phòng đã xác thực")
        // on below line we are setting icons.
        dataSet.setDrawIcons(true)

        // on below line we are setting slice for pie
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors to list
        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.piechar2))
        colors.add(resources.getColor(R.color.piechar3))

        // on below line we are setting colors.
        dataSet.colors = colors

        // on below line we are setting pie data set
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        binding.pieChart2.setData(data)

        // undo all highlights
        binding.pieChart2.highlightValues(null)
        binding.pieChart2.centerText = "Phòng đã xác thực"
        binding.pieChart2.animate()
        // loading chart
        binding.pieChart2.invalidate()

    }

    fun barChartAll() {
        var visit = ArrayList<BarEntry>()
        for (i in 0 until copyTimeRoom.size) {
            visit.add(BarEntry(i.toFloat(), countRoom[i]))
        }
        val xAxis = binding.barChart.getXAxis()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.labelCount = copyTimeRoom.size // important
        xAxis.valueFormatter = object : ValueFormatter() {
            override
            fun getFormattedValue(value: Float): String {
                // value is x as index
                return copyTimeRoom[value.toInt()]
            }
        }
        var barDataSet = BarDataSet(visit, "Số lượng phòng thuê")
        var partColors = ArrayList<Int>()
        partColors.add(Color.BLUE)
        barDataSet.setColors(partColors)
        var textColors = ArrayList<Int>()
        textColors.add(Color.BLACK)
        barDataSet.setValueTextColors(textColors)
        barDataSet.setValueTextSize(16f)
        var barData = BarData(barDataSet)
        binding.barChart.setFitBars(true)
        binding.barChart.data = barData
        binding.barChart.description.text = "Số lượng phòng thuê"
        binding.barChart.animateY(2000)
        binding.barChart.invalidate()
    }

    fun barChartVerified() {
        var visit = ArrayList<BarEntry>()
        for (i in 0 until copyTimeRoomVerified.size) {
            visit.add(BarEntry(i.toFloat(), countRoomVerified[i]))
        }
        val xAxis = binding.barChart2.getXAxis()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.labelCount = copyTimeRoomVerified.size // important
        xAxis.valueFormatter = object : ValueFormatter() {
            override
            fun getFormattedValue(value: Float): String {
                // value is x as index
                return copyTimeRoomVerified[value.toInt()]
            }
        }
        var barDataSet = BarDataSet(visit, "Số lượng phòng thuê đã xác thực")
        var partColors = ArrayList<Int>()
        partColors.add(Color.BLUE)
        barDataSet.setColors(partColors)
        var textColors = ArrayList<Int>()
        textColors.add(Color.BLACK)
        barDataSet.setValueTextColors(textColors)
        barDataSet.setValueTextSize(16f)
        var barData = BarData(barDataSet)
        binding.barChart2.setFitBars(true)
        binding.barChart2.data = barData
        binding.barChart2.description.text = "Số lượng phòng thuê đã xác thực"
        binding.barChart2.animateY(2000)
        binding.barChart2.invalidate()
    }

    fun barChartRevenue() {
        var visit = ArrayList<BarEntry>()
        for (i in 0 until copyTimeRoomVerified.size) {
            visit.add(BarEntry(i.toFloat(), copyRevenueRoom[i].toFloat()))
        }
        val xAxis = binding.barChart3.getXAxis()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.labelCount = copyTimeRoomVerified.size // important
        xAxis.valueFormatter = object : ValueFormatter() {
            override
            fun getFormattedValue(value: Float): String {
                // value is x as index
                return copyTimeRoomVerified[value.toInt()]
            }
        }
        var barDataSet = BarDataSet(visit, "Doanh thu")
        var partColors = ArrayList<Int>()
        partColors.add(Color.BLUE)
        barDataSet.setColors(partColors)
        var textColors = ArrayList<Int>()
        textColors.add(Color.BLACK)
        barDataSet.setValueTextColors(textColors)
        barDataSet.setValueTextSize(16f)
        var barData = BarData(barDataSet)
        binding.barChart3.setFitBars(true)
        binding.barChart3.data = barData
        binding.barChart3.description.text = "Doanh thu"
        binding.barChart3.animateY(2000)
        binding.barChart3.invalidate()
    }
}