package com.rach.lawyerapp.ui.home.data.dataSource

import com.rach.lawyerapp.ui.home.data.model.DiscountModel
import com.rach.lawyerapp.ui.home.data.model.LawyerModel

object DisCountDataSource {
    val discountData = listOf(
        DiscountModel(value = 50,  discount = 5),
        DiscountModel(value = 100, discount = 10),
        DiscountModel(value = 200, discount = 10),
        DiscountModel(value = 500, discount = 15),
        DiscountModel(value = 1000,discount = 20),
    )

}