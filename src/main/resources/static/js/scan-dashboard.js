const loadingState =
    document.getElementById("loadingState");

const missingState =
    document.getElementById("missingState");

const availableState =
    document.getElementById("availableState");


const reportPath =
    document.getElementById("reportPath");

const lastModified =
    document.getElementById("lastModified");


const refreshButton =
    document.getElementById("refreshScanButton");


refreshButton.addEventListener(
    "click",
    checkScanStatus
);


async function checkScanStatus() {

    showLoading();

    try {

        const response =
            await fetch(
                "/flattire/api/scan/status"
            );


        if (!response.ok) {

            throw new Error(
                `Request failed: ${response.status}`
            );
        }


        const scan =
            await response.json();


        if (scan.reportAvailable) {

            showAvailable(scan);

        } else {

            showMissing();
        }

    } catch (error) {

        console.error(
            "Unable to check FlatTire scan status:",
            error
        );


        showMissing();
    }
}


function showLoading() {

    loadingState
        .classList
        .remove("hidden");


    missingState
        .classList
        .add("hidden");


    availableState
        .classList
        .add("hidden");
}


function showMissing() {

    loadingState
        .classList
        .add("hidden");


    missingState
        .classList
        .remove("hidden");


    availableState
        .classList
        .add("hidden");
}


function showAvailable(scan) {

    loadingState
        .classList
        .add("hidden");


    missingState
        .classList
        .add("hidden");


    availableState
        .classList
        .remove("hidden");


    reportPath.textContent =
        scan.reportPath ?? "Unknown";


    lastModified.textContent =
        formatDate(scan.lastModified);
}


function formatDate(date) {

    if (!date) {
        return "Unknown";
    }


    return new Date(date)
        .toLocaleString();
}


checkScanStatus();