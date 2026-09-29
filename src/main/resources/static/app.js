const API = "/api/campaigns";

document.addEventListener("DOMContentLoaded", () => {
    loadCampaigns();
    loadSummary();
    loadAlerts();
});


async function loadCampaigns() {

    try {

        const response = await fetch(API);

        if (!response.ok) {
            throw new Error("Failed to load campaigns");
        }

        const campaigns = await response.json();

        displayCampaigns(campaigns);

    } catch (error) {

        console.error(error);

        document.getElementById("campaignTableBody").innerHTML =
            `<tr>
                <td colspan="9">Unable to load campaigns</td>
            </tr>`;
    }
}


function displayCampaigns(campaigns) {

    const tableBody =
        document.getElementById("campaignTableBody");

    tableBody.innerHTML = "";

    if (campaigns.length === 0) {

        tableBody.innerHTML =
            `<tr>
                <td colspan="9">No campaigns found</td>
            </tr>`;

        return;
    }

    campaigns.forEach(campaign => {

        const row = document.createElement("tr");

        row.innerHTML = `
            <td>${campaign.id}</td>
            <td>${campaign.campaignName}</td>
            <td>${campaign.advertiser}</td>
            <td>${campaign.platform || "-"}</td>
            <td>₹${campaign.budget || 0}</td>
            <td>${campaign.status || "-"}</td>
            <td>${campaign.impressions || 0}</td>
            <td>${campaign.clicks || 0}</td>
            <td>${campaign.conversions || 0}</td>
        `;

        tableBody.appendChild(row);
    });
}


async function loadSummary() {

    try {

        const response =
            await fetch(`${API}/summary`);

        const summary =
            await response.json();

        document.getElementById("totalCampaigns").textContent =
            summary.totalCampaigns;

        document.getElementById("activeCampaigns").textContent =
            summary.activeCampaigns;

        document.getElementById("completedCampaigns").textContent =
            summary.completedCampaigns;

        document.getElementById("totalBudget").textContent =
            `₹${summary.totalBudget}`;

        document.getElementById("totalClicks").textContent =
            summary.totalClicks;

        document.getElementById("totalConversions").textContent =
            summary.totalConversions;

    } catch (error) {

        console.error("Summary error:", error);
    }
}


async function searchCampaigns() {

    const keyword =
        document.getElementById("searchInput").value.trim();

    if (!keyword) {

        loadCampaigns();

        return;
    }

    try {

        const response =
            await fetch(
                `${API}/search?keyword=${encodeURIComponent(keyword)}`
            );

        const campaigns =
            await response.json();

        displayCampaigns(campaigns);

    } catch (error) {

        console.error("Search error:", error);
    }
}


async function filterByStatus() {

    const status =
        document.getElementById("statusFilter").value;

    if (!status) {

        loadCampaigns();

        return;
    }

    try {

        const response =
            await fetch(`${API}/status/${status}`);

        const campaigns =
            await response.json();

        displayCampaigns(campaigns);

    } catch (error) {

        console.error("Status filter error:", error);
    }
}


async function loadAlerts() {

    try {

        const response =
            await fetch(`${API}/alerts`);

        const alerts =
            await response.json();

        const container =
            document.getElementById("alertsContainer");

        container.innerHTML = "";

        if (alerts.length === 0) {

            container.innerHTML =
                "<p>No active alerts or exceptions.</p>";

            return;
        }

        alerts.forEach(alert => {

            const div =
                document.createElement("div");

            div.className = "alert";

            div.innerHTML = `
                <strong>${alert.severity}</strong>
                - ${alert.campaignName}
                <br>
                ${alert.message}
            `;

            container.appendChild(div);
        });

    } catch (error) {

        console.error("Alert error:", error);
    }
}