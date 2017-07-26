$(function () {
    $("table").tablesorter({
        theme: "bootstrap",
        sortReset: true,
        widthFixed: true,
        headerTemplate: '{content} {icon}',
        widgets: ["uitheme", "filter"],
        widgetOptions: {
            filter_cssFilter: "form-control",
            filter_saveFilters: true
        }
    })
            .tablesorterPager({
                size: 5,
                container: $(".ts-pager"),
                // possible variables: {page}, {totalPages}, {filteredPages}, {startRow}, {endRow}, {filteredRows} and {totalRows}
                output: '{startRow} - {endRow} / {filteredRows} ({totalRows})'
            });
});