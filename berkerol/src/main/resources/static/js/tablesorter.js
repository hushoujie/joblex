$(function () {
    $("table").tablesorter({
        theme: "bootstrap",
        sortReset: true,
        headerTemplate: '{content} {icon}',
        widgets: ["uitheme", "saveSort", "resizable", "filter", "mark", "print", "stickyHeaders"],
        widgetOptions: {
            stickyHeaders_offset: '.navbar-fixed-top',
            filter_cssFilter: "form-control",
            filter_external: '.search',
            filter_reset: '.reset_filter',
            filter_saveFilters: true,
            print_styleSheet: '../static/css/theme.bootstrap.css'
        }
    })
            .tablesorterPager({
                size: 5,
                container: $(".ts-pager"),
                // possible variables: {page}, {totalPages}, {filteredPages}, {startRow}, {endRow}, {filteredRows} and {totalRows}
                output: '{startRow} - {endRow} / {filteredRows} ({totalRows})'
            });
    $('.reset_sort').click(function () {
        $('table').trigger('saveSortReset').trigger("sortReset");
        return false;
    });
    $('.print').click(function () {
        $('table').trigger('printTable');
    });
});
