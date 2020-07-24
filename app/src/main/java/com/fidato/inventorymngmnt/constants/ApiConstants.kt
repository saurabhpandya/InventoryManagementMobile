package com.fidato.inventorymngmnt.constants

class ApiConstants {

    companion object {
        const val API_V1_MASTER = "master/api/v1"
        const val API_V1_SUPPLIER = "supplier/api/v1"
        const val API_V1_CUSTOMER = "customer/api/v1"
        const val API_V1_ORDER = "order/api/v1"

        const val ENDPOINT_MASTER_USERTYPE = "userType"
        const val ENDPOINT_USERTYPE_GET = "$ENDPOINT_MASTER_USERTYPE/{type}"
        const val ENDPOINT_USERTYPE_SAVE = "$ENDPOINT_MASTER_USERTYPE/saveUserType"
        const val ENDPOINT_USERTYPE_UPDATE = "$ENDPOINT_MASTER_USERTYPE/updateUserType"
        const val ENDPOINT_USERTYPE_DELETE = "$ENDPOINT_MASTER_USERTYPE/deleteUserType"

        const val ENDPOINT_MASTER_CATEGORY = "${API_V1_MASTER}/category"
        const val ENDPOINT_CATEGORY_GET = "$ENDPOINT_MASTER_CATEGORY/{id}"
        const val ENDPOINT_CATEGORY_SAVE = "$ENDPOINT_MASTER_CATEGORY/saveCategory"
        const val ENDPOINT_CATEGORY_UPDATE = "$ENDPOINT_MASTER_CATEGORY/updateCategory"
        const val ENDPOINT_CATEGORY_DELETE = "$ENDPOINT_MASTER_CATEGORY/deleteCategory"

        const val ENDPOINT_MASTER_SUB_CATEGORY = "${API_V1_MASTER}/subCategory"
        const val ENDPOINT_MASTER_SUB_CATEGORY_LEAFS = "${API_V1_MASTER}/subCategoryLeafs"
        const val ENDPOINT_SUB_CATEGORY_GET = "$ENDPOINT_MASTER_SUB_CATEGORY/{id}"
        const val ENDPOINT_SUB_CATEGORY_BY_CAT_ID = "$ENDPOINT_MASTER_SUB_CATEGORY/category/{catId}"
        const val ENDPOINT_SUB_CATEGORY_BY_SUBCAT_ID =
            "$ENDPOINT_MASTER_SUB_CATEGORY/{catId}/{subCatId}"
        const val ENDPOINT_SUB_CATEGORY_SAVE = "$ENDPOINT_MASTER_SUB_CATEGORY/saveSubCategory"
        const val ENDPOINT_SUB_CATEGORY_UPDATE = "$ENDPOINT_MASTER_SUB_CATEGORY/updateSubCategory"
        const val ENDPOINT_SUB_CATEGORY_DELETE = "$ENDPOINT_MASTER_SUB_CATEGORY/deleteSubCategory"

        const val ENDPOINT_MASTER_PRODUCT = "${API_V1_MASTER}/product"
        const val ENDPOINT_PRODUCT_GET = "$ENDPOINT_MASTER_PRODUCT/{id}"
        const val ENDPOINT_PRODUCT_BY_SUBCAT_ID = "$ENDPOINT_MASTER_PRODUCT/subCat/{subCatId}"
        const val ENDPOINT_PRODUCT_SAVE = "$ENDPOINT_MASTER_PRODUCT/saveProduct"
        const val ENDPOINT_PRODUCT_UPDATE = "$ENDPOINT_MASTER_PRODUCT/updateProduct"
        const val ENDPOINT_PRODUCT_DELETE = "$ENDPOINT_MASTER_PRODUCT/deleteProduct"

        const val ENDPOINT_MASTER_PRODUCT_VARIANT = "${API_V1_MASTER}/product_variant"
        const val ENDPOINT_PRODUCT_VARIANT_GET = "$ENDPOINT_MASTER_PRODUCT_VARIANT/{id}"
        const val ENDPOINT_PRODUCT_VARIANT_BY_PRODUCT_ID =
            "$ENDPOINT_MASTER_PRODUCT_VARIANT/product/{product_id}"
        const val ENDPOINT_PRODUCT_VARIANT_SAVE =
            "$ENDPOINT_MASTER_PRODUCT_VARIANT/saveProductVariant"
        const val ENDPOINT_PRODUCT_VARIANT_UPDATE =
            "$ENDPOINT_MASTER_PRODUCT_VARIANT/updateProductVariant"
        const val ENDPOINT_PRODUCT_VARIANT_DELETE =
            "$ENDPOINT_MASTER_PRODUCT_VARIANT/deleteProductVariant"

        const val ENDPOINT_MASTER_CURRENCY = "${API_V1_MASTER}/currency"
        const val ENDPOINT_CURRENCY_GET = "$ENDPOINT_MASTER_CURRENCY/{id}"
        const val ENDPOINT_CURRENCY_SAVE = "$ENDPOINT_MASTER_CURRENCY/saveCurrency"
        const val ENDPOINT_CURRENCY_UPDATE = "$ENDPOINT_MASTER_CURRENCY/updateCurrency"
        const val ENDPOINT_CURRENCY_DELETE = "$ENDPOINT_MASTER_CURRENCY/deleteCurrency"

        const val ENDPOINT_LOCATION = "location"
        const val ENDPOINT_LOCATION_GET = "$ENDPOINT_LOCATION/{id}"
        const val ENDPOINT_LOCATION_SAVE = "$ENDPOINT_LOCATION/saveLocation"
        const val ENDPOINT_LOCATION_UPDATE = "$ENDPOINT_LOCATION/updateLocation"
        const val ENDPOINT_LOCATION_DELETE = "$ENDPOINT_LOCATION/deleteLocation"

        const val ENDPOINT_ORDER = "${API_V1_ORDER}/order"
        const val ENDPOINT_ORDER_GET = "$ENDPOINT_ORDER/{id}"
        const val ENDPOINT_ORDER_SAVE = "$ENDPOINT_ORDER/saveOrder"
        const val ENDPOINT_ORDER_UPDATE = "$ENDPOINT_ORDER/updateOrder"
        const val ENDPOINT_ORDER_DELETE = "$ENDPOINT_ORDER/deleteOrder"

        const val ENDPOINT_SUPPLIER = "${API_V1_SUPPLIER}/supplier"
        const val ENDPOINT_SUPPLIER_GET = "$ENDPOINT_SUPPLIER/{id}"
        const val ENDPOINT_SUPPLIER_SAVE = "$ENDPOINT_SUPPLIER/saveSupplier"
        const val ENDPOINT_SUPPLIER_UPDATE = "$ENDPOINT_SUPPLIER/updateSupplier"
        const val ENDPOINT_SUPPLIER_DELETE = "$ENDPOINT_SUPPLIER/deleteSupplier"

        const val ENDPOINT_CUSTOMER = "${API_V1_CUSTOMER}/customer"
        const val ENDPOINT_CUSTOMER_GET = "$ENDPOINT_CUSTOMER/{id}"
        const val ENDPOINT_CUSTOMER_SAVE = "$ENDPOINT_CUSTOMER/saveCustomer"
        const val ENDPOINT_CUSTOMER_UPDATE = "$ENDPOINT_CUSTOMER/updateCustomer"
        const val ENDPOINT_CUSTOMER_DELETE = "$ENDPOINT_CUSTOMER/deleteCustomer"


    }

}