const loadingState =
    document.getElementById("loadingState");

const missingState =
    document.getElementById("missingState");

const availableState =
    document.getElementById("availableState");

const summarySection =
    document.getElementById("summarySection");

const vulnerabilitySection =
    document.getElementById("vulnerabilitySection");

const vulnerabilityResults =
    document.getElementById("vulnerabilityResults");

const reportPath =
    document.getElementById("reportPath");

const lastModified =
    document.getElementById("lastModified");

const refreshButton =
    document.getElementById("refreshScanButton");

const totalDependencies =
    document.getElementById("totalDependencies");

const vulnerableDependencies =
    document.getElementById("vulnerableDependencies");

const knownCves =
    document.getElementById("knownCves");

const highestSeverity =
    document.getElementById("highestSeverity");


refreshButton.addEventListener(
    "click",
    refreshDashboard
);


async function refreshDashboard() {

    showLoading();

    try {

        const [
            statusResponse,
            summaryResponse,
            vulnerabilityResponse
        ] = await Promise.all([

            fetch(
                "/flattire/api/scan/status"
            ),

            fetch(
                "/flattire/api/scan/summary"
            ),

            fetch(
                "/flattire/api/scan/vulnerabilities"
            )

        ]);


        if (
            !statusResponse.ok
            || !summaryResponse.ok
            || !vulnerabilityResponse.ok
        ) {

            throw new Error(
                "Unable to load scan dashboard"
            );
        }


        const scanStatus =
            await statusResponse.json();

        const scanSummary =
            await summaryResponse.json();

        const vulnerabilities =
            await vulnerabilityResponse.json();


        if (!scanStatus.reportAvailable) {

            showMissing();

            return;
        }


        showAvailable(
            scanStatus,
            scanSummary,
            vulnerabilities
        );

    } catch (error) {

        console.error(
            "Unable to refresh FlatTire dashboard:",
            error
        );

        showMissing();
    }
}


function showLoading() {

    loadingState.classList.remove("hidden");

    missingState.classList.add("hidden");

    availableState.classList.add("hidden");

    summarySection.classList.add("hidden");

    vulnerabilitySection.classList.add("hidden");
}


function showMissing() {

    loadingState.classList.add("hidden");

    missingState.classList.remove("hidden");

    availableState.classList.add("hidden");

    summarySection.classList.add("hidden");

    vulnerabilitySection.classList.add("hidden");
}


function showAvailable(
    scanStatus,
    scanSummary,
    vulnerabilities
) {

    loadingState.classList.add("hidden");

    missingState.classList.add("hidden");

    availableState.classList.remove("hidden");

    summarySection.classList.remove("hidden");


    reportPath.textContent =
        scanStatus.reportPath ?? "Unknown";

    lastModified.textContent =
        formatDate(
            scanStatus.lastModified
        );


    totalDependencies.textContent =
        scanSummary.totalDependencies;

    vulnerableDependencies.textContent =
        scanSummary.vulnerableDependencies;

    knownCves.textContent =
        scanSummary.knownCves;


    const severity =
        scanSummary.highestSeverity ?? "NONE";


    highestSeverity.textContent =
        severity;

    highestSeverity.className =
        "summary-value severity "
        + severity.toLowerCase();


    renderVulnerabilities(
        vulnerabilities
    );
}


function renderVulnerabilities(
    dependencies
) {

    vulnerabilityResults.innerHTML = "";


    if (!dependencies.length) {

        vulnerabilitySection.classList.add(
            "hidden"
        );

        return;
    }


    vulnerabilitySection.classList.remove(
        "hidden"
    );


    dependencies.forEach(dependency => {

        const card =
            document.createElement("article");

        card.className =
            "vulnerability-card";


        const header =
            document.createElement("div");

        header.className =
            "vulnerability-card-header";


        const heading =
            document.createElement("div");


        const dependencyName =
            document.createElement("h3");

        dependencyName.className =
            "dependency-name";

        dependencyName.textContent =
            dependency.dependency;


        const meta =
            document.createElement("div");

        meta.className =
            "vulnerability-meta";

        meta.textContent =
            `${dependency.vulnerabilityCount} known ${
                dependency.vulnerabilityCount === 1
                    ? "vulnerability"
                    : "vulnerabilities"
            }`;


        heading.appendChild(
            dependencyName
        );

        heading.appendChild(
            meta
        );


        const severityBadge =
            document.createElement("span");

        severityBadge.className =
            "severity-badge "
            + dependency.highestSeverity.toLowerCase();

        severityBadge.textContent =
            dependency.highestSeverity;


        header.appendChild(
            heading
        );

        header.appendChild(
            severityBadge
        );


        const cveList =
            document.createElement("div");

        cveList.className =
            "cve-list";


        dependency.vulnerabilities
            .slice(0, 3)
            .forEach(vulnerability => {

                cveList.appendChild(
                    createCveRow(
                        vulnerability
                    )
                );
            });


        card.appendChild(
            header
        );

        card.appendChild(
            cveList
        );


        const remaining =
            dependency.vulnerabilities.length - 3;


        if (remaining > 0) {

            const more =
                document.createElement("div");

            more.className =
                "more-vulnerabilities";

            more.textContent =
                `+${remaining} more vulnerabilities`;

            card.appendChild(
                more
            );
        }


        const detailsButton =
            document.createElement("button");

        detailsButton.className =
            "details-button";

        detailsButton.type =
            "button";

        detailsButton.textContent =
            "View Details";


        detailsButton.addEventListener(
            "click",
            () => toggleDetails(
                card,
                dependency,
                detailsButton
            )
        );


        card.appendChild(
            detailsButton
        );


        vulnerabilityResults.appendChild(
            card
        );
    });
}


function createCveRow(
    vulnerability
) {

    const row =
        document.createElement("div");

    row.className =
        "cve-row";


    const name =
        document.createElement("span");

    name.className =
        "cve-name";

    name.textContent =
        vulnerability.name;


    const severity =
        document.createElement("span");

    severity.className =
        "cve-severity";

    severity.textContent =
        vulnerability.severity;


    const score =
        document.createElement("span");

    score.className =
        "cve-score";

    score.textContent =
        Number(
            vulnerability.cvssScore
        ).toFixed(1);


    row.appendChild(name);

    row.appendChild(severity);

    row.appendChild(score);


    return row;
}


function toggleDetails(
    card,
    dependency,
    button
) {

    const existingDetails =
        card.querySelector(
            ".vulnerability-details"
        );


    if (existingDetails) {

        existingDetails.remove();

        button.textContent =
            "View Details";

        return;
    }


    const details =
        document.createElement("div");

    details.className =
        "vulnerability-details";


    dependency.vulnerabilities
        .forEach(vulnerability => {

            const detail =
                document.createElement("div");

            detail.className =
                "vulnerability-detail";


            const title =
                document.createElement("strong");

            title.textContent =
                `${vulnerability.name} · `
                + `${vulnerability.severity} · `
                + `${Number(vulnerability.cvssScore).toFixed(1)}`;


            const description =
                document.createElement("p");

            description.textContent =
                vulnerability.description;


            detail.appendChild(
                title
            );

            detail.appendChild(
                description
            );

            details.appendChild(
                detail
            );
        });


    card.insertBefore(
        details,
        button
    );


    button.textContent =
        "Hide Details";
}


function formatDate(date) {

    if (!date) {
        return "Unknown";
    }

    return new Date(date)
        .toLocaleString();
}


refreshDashboard();