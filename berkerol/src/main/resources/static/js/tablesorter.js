$(function () {
    $.tablesorter.themes.bootstrap = {
        table: '',
        caption: 'caption',
        header: 'bootstrap-header', // give the header a gradient background (theme.bootstrap_2.css)
        sortNone: '',
        sortAsc: '',
        sortDesc: '',
        active: '', // applied when column is sorted
        hover: '', // custom css required - a defined bootstrap style may not override other classes
        icons: '', // add "icon-white" to make them white; this icon class is added to the <i></i> in the header
        iconSortNone: 'bootstrap-icon-unsorted', // class name added to icon when column is not sorted
        iconSortAsc: 'glyphicon glyphicon-chevron-up', // class name added to icon when column has ascending sort
        iconSortDesc: 'glyphicon glyphicon-chevron-down', // class name added to icon when column has descending sort
        filterRow: '', // filter row class; use widgetOptions.filter_cssFilter for the input/select element
        footerRow: '',
        footerCells: '',
        even: '', // even row zebra striping
        odd: ''  // odd row zebra striping
    };
    $("table").tablesorter({
        theme: "bootstrap",
        widthFixed: true,
        headerTemplate: '{content} {icon}', // new in v2.7. Needed to add the bootstrap icon!
        widgets: ["uitheme", "filter"],
        //widgets : [ "uitheme", "filter", "columns", "zebra" ],
        widgetOptions: {
            // using the default zebra striping class name, so it actually isn't included in the theme variable above
            // this is ONLY needed for bootstrap theming if you are using the filter widget, because rows are hidden
            zebra: ["even", "odd"],
            // class names added to columns when sorted
            columns: ["primary", "secondary", "tertiary"],
            // reset filters button
            filter_reset: ".reset",
            // extra css class name (string or array) added to the filter element (input or select)
            filter_cssFilter: "form-control"
        }
    })
            .tablesorterPager({
                // target the pager markup - see the HTML block below
                container: $(".ts-pager"),
                // target the pager page select dropdown - choose a page
                cssGoto: ".pagenum",
                // remove rows from the table to speed up the sort of large tables.
                // setting this to false, only hides the non-visible rows; needed if you plan to add/remove rows with the pager enabled.
                removeRows: false,
                // output string - default is '{page}/{totalPages}';
                // possible variables: {page}, {totalPages}, {filteredPages}, {startRow}, {endRow}, {filteredRows} and {totalRows}
                output: '{startRow} - {endRow} / {filteredRows} ({totalRows})'
            });
});