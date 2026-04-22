const checkoutForm = document.getElementById("checkout-form");
const checkoutResponseElement = document.getElementById("checkout-response");
const orderResponseElement = document.getElementById("order-response");
const servicesGridElement = document.getElementById("services-grid");
const refreshServicesButton = document.getElementById("refresh-services-button");
const openJaegerButton = document.getElementById("open-jaeger-button");
const loadLastOrderButton = document.getElementById("load-last-order-button");
const scenarioButtons = Array.from(document.querySelectorAll(".scenario-button"));

let lastOrderId = null;

async function callCheckout(paymentScenario) {
    const payload = {
        customerId: document.getElementById("customer-id").value,
        itemId: document.getElementById("item-id").value,
        quantity: Number(document.getElementById("quantity").value),
        paymentScenario
    };

    try {
        const response = await fetch("/api/v1/checkout", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });

        const body = await response.json();
        renderJson(checkoutResponseElement, body);

        if (response.ok && body.orderId) {
            lastOrderId = body.orderId;
            await loadOrder(body.orderId);
        } else {
            orderResponseElement.textContent = "Order snapshot is unavailable for the last response.";
        }
    } catch (error) {
        renderJson(checkoutResponseElement, { error: error.message });
    }
}

async function loadOrder(orderId) {
    if (!orderId) {
        orderResponseElement.textContent = "No order ID available yet.";
        return;
    }

    try {
        const response = await fetch(`/api/v1/control-panel/orders/${orderId}`);
        const body = await response.json();
        renderJson(orderResponseElement, body);
    } catch (error) {
        renderJson(orderResponseElement, { error: error.message });
    }
}

async function refreshServices() {
    try {
        const response = await fetch("/api/v1/control-panel/services");
        const services = await response.json();
        servicesGridElement.innerHTML = "";

        services.forEach((service) => {
            const statusClass = service.status === "UP" ? "up" : service.status === "DOWN" ? "down" : "unknown";
            const card = document.createElement("article");
            card.className = "service-card";
            card.innerHTML = `
                <h3>${service.serviceName}</h3>
                <span class="service-url">${service.baseUrl}</span>
                <span class="service-status ${statusClass}">${service.status}</span>
            `;
            servicesGridElement.appendChild(card);
        });
    } catch (error) {
        servicesGridElement.innerHTML = `<article class="service-card"><h3>Service status</h3><span class="service-url">fetch failed</span><span class="service-status down">${error.message}</span></article>`;
    }
}

function renderJson(target, payload) {
    target.textContent = JSON.stringify(payload, null, 2);
}

checkoutForm.addEventListener("submit", async (event) => {
    event.preventDefault();
    await callCheckout(document.getElementById("payment-scenario").value);
});

scenarioButtons.forEach((button) => {
    button.addEventListener("click", async () => {
        document.getElementById("payment-scenario").value = button.dataset.scenario;
        await callCheckout(button.dataset.scenario);
    });
});

refreshServicesButton.addEventListener("click", refreshServices);
openJaegerButton.addEventListener("click", () => window.open("http://localhost:16686", "_blank", "noopener,noreferrer"));
loadLastOrderButton.addEventListener("click", () => loadOrder(lastOrderId));

refreshServices();
