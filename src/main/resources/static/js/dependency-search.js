const DEFAULT_VERSION_LIMIT = 50;
const COLLAPSED_VERSION_LIMIT = 8;


let allVersions = [];
let selectedResultCard = null;
let showingAllVersions = false;


const searchInput =
    document.getElementById("searchInput");


const searchButton =
    document.getElementById("searchButton");


const status =
    document.getElementById("status");


const searchSection =
    document.getElementById("searchSection");


const searchResults =
    document.getElementById("searchResults");


const searchResultCount =
    document.getElementById("searchResultCount");


const versionSection =
    document.getElementById("versionSection");


const selectedArtifact =
    document.getElementById("selectedArtifact");


const versionCount =
    document.getElementById("versionCount");


const versionResults =
    document.getElementById("versionResults");


const showAllButton =
    document.getElementById("showAllButton");


searchButton.addEventListener(
    "click",
    searchDependencies
);


searchInput.addEventListener(
    "keydown",
    event => {

        if (event.key === "Enter") {

            searchDependencies();
        }

    }
);


showAllButton.addEventListener(
    "click",
    toggleAllVersions
);


async function searchDependencies() {

    const query =
        searchInput.value.trim();


    if (query.length < 2) {

        setStatus(
            "Enter at least 2 characters."
        );

        return;
    }


    setStatus(
        "Searching Maven Central..."
    );


    searchSection
        .classList
        .add("hidden");


    versionSection
        .classList
        .add("hidden");


    selectedResultCard = null;
    showingAllVersions = false;
    allVersions = [];


    try {

        const response =
            await fetch(
                `/flattire/api/search?query=${encodeURIComponent(query)}`
            );


        if (!response.ok) {

            throw new Error(
                `Search failed: ${response.status}`
            );
        }


        const results =
            await response.json();


        renderSearchResults(results);


        setStatus("");

    } catch (error) {

        console.error(error);


        setStatus(
            "Unable to search Maven Central."
        );
    }
}


function renderSearchResults(results) {

    searchResults.innerHTML = "";


    searchResultCount.textContent =
        `${results.length} ${results.length === 1 ? "result" : "results"}`;


    searchSection
        .classList
        .remove("hidden");


    if (results.length === 0) {

        searchResults.textContent =
            "No dependencies found.";

        return;
    }


    results.forEach(result => {

        const card =
            document.createElement("div");


        card.className =
            "result-card";


        const artifact =
            document.createElement("div");


        artifact.className =
            "artifact-name";


        artifact.textContent =
            `${result.groupId}:${result.artifactId}`;


        const latest =
            document.createElement("div");


        latest.className =
            "latest-version";


        latest.textContent =
            `Latest: ${result.latestVersion}`;


        card.appendChild(artifact);

        card.appendChild(latest);


        card.addEventListener(
            "click",
            () => toggleVersions(
                card,
                result.groupId,
                result.artifactId
            )
        );


        searchResults.appendChild(card);
    });
}


function toggleVersions(
    card,
    groupId,
    artifactId
) {

    if (selectedResultCard === card) {

        card.classList.remove("selected");

        selectedResultCard = null;
        showingAllVersions = false;
        allVersions = [];

        versionSection
            .classList
            .add("hidden");

        showAllButton
            .classList
            .add("hidden");

        showAllButton.textContent =
            "Show All Versions";

        return;
    }


    if (selectedResultCard) {

        selectedResultCard
            .classList
            .remove("selected");
    }


    selectedResultCard = card;

    card.classList.add("selected");


    loadVersions(
        groupId,
        artifactId
    );
}


async function loadVersions(
    groupId,
    artifactId
) {

    showingAllVersions = false;


    setStatus(
        "Loading versions..."
    );


    try {

        const url =
            "/flattire/api/versions"
            + `?groupId=${encodeURIComponent(groupId)}`
            + `&artifactId=${encodeURIComponent(artifactId)}`;


        const response =
            await fetch(url);


        if (!response.ok) {

            throw new Error(
                `Version request failed: ${response.status}`
            );
        }


        allVersions =
            await response.json();


        selectedArtifact.textContent =
            `${groupId}:${artifactId}`;


        versionCount.textContent =
            `${allVersions.length} versions available`;


        renderVersions(
            allVersions.slice(
                0,
                COLLAPSED_VERSION_LIMIT
            )
        );


        versionSection
            .classList
            .remove("hidden");


        if (
            allVersions.length >
            COLLAPSED_VERSION_LIMIT
        ) {

            showAllButton
                .classList
                .remove("hidden");

            showAllButton.textContent =
                "Show All Versions";

        } else {

            showAllButton
                .classList
                .add("hidden");
        }


        setStatus("");


    } catch (error) {

        console.error(error);


        setStatus(
            "Unable to load dependency versions."
        );
    }
}


function renderVersions(versions) {

    versionResults.innerHTML = "";


    versions.forEach(result => {

        const card =
            document.createElement("div");


        card.className =
            "version-card";


        card.textContent =
            result.version;


        versionResults.appendChild(card);
    });
}


function toggleAllVersions() {

    showingAllVersions =
        !showingAllVersions;


    if (showingAllVersions) {

        renderVersions(
            allVersions
        );


        showAllButton.textContent =
            "Collapse Versions";

        return;
    }


    renderVersions(
        allVersions.slice(
            0,
            COLLAPSED_VERSION_LIMIT
        )
    );


    showAllButton.textContent =
        "Show All Versions";
}


function setStatus(message) {

    status.textContent =
        message;
}