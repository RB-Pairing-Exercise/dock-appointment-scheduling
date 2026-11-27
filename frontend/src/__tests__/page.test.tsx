import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import Home from "@/app/page";
import { todayIsoDate } from "@/lib/appointments";

const alphaAppointments = [
  {
    id: 1,
    dockId: 1,
    dockName: "Dock 1",
    dockType: "DRY_VAN",
    carrierId: "carrier-alpha",
    carrierName: "Alpha Freight",
    trailerType: "DRY_VAN",
    startTime: `${todayIsoDate()}T08:00:00`,
    endTime: `${todayIsoDate()}T09:00:00`,
    status: "SCHEDULED",
  },
];

const betaAppointments = [
  {
    id: 2,
    dockId: 2,
    dockName: "Dock 2",
    dockType: "DRY_VAN",
    carrierId: "carrier-beta",
    carrierName: "Beta Logistics",
    trailerType: "DRY_VAN",
    startTime: `${todayIsoDate()}T10:30:00`,
    endTime: `${todayIsoDate()}T11:30:00`,
    status: "SCHEDULED",
  },
];

function mockFetchImplementation(fetchMock: ReturnType<typeof vi.fn>) {
  fetchMock.mockImplementation((url: string) => {
    const parsed = new URL(url);
    const carrierId = parsed.searchParams.get("carrierId");
    const date = parsed.searchParams.get("date");

    let body: unknown[] = [];
    if (date === todayIsoDate()) {
      if (carrierId === "carrier-alpha") body = alphaAppointments;
      if (carrierId === "carrier-beta") body = betaAppointments;
    }

    return Promise.resolve({
      ok: true,
      json: () => Promise.resolve(body),
    });
  });
}

describe("Home page", () => {
  beforeEach(() => {
    const fetchMock = vi.fn();
    vi.stubGlobal("fetch", fetchMock);
    mockFetchImplementation(fetchMock);
  });

  afterEach(() => {
    vi.unstubAllGlobals();
    vi.restoreAllMocks();
  });

  it("loads appointments for the default carrier (Alpha Freight) on initial render", async () => {
    render(<Home />);

    expect(await screen.findByText("Dock 1")).toBeInTheDocument();
    expect(screen.getByText(/Alpha Freight.*Appointments/)).toBeInTheDocument();

    expect(fetch).toHaveBeenCalledWith(
      expect.stringContaining("carrierId=carrier-alpha"),
    );
  });

  it("switching the carrier dropdown fetches and renders that carrier's appointments", async () => {
    const user = userEvent.setup();
    render(<Home />);

    expect(await screen.findByText("Dock 1")).toBeInTheDocument();

    const select = screen.getByLabelText("Acting as carrier");
    await user.selectOptions(select, "carrier-beta");

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith(
        expect.stringContaining("carrierId=carrier-beta"),
      );
    });

    expect(await screen.findByText("Dock 2")).toBeInTheDocument();
    expect(screen.queryByText("Dock 1")).not.toBeInTheDocument();
  });

  it("changing the date re-fetches and shows the empty state when no appointments are returned", async () => {
    const user = userEvent.setup();
    render(<Home />);

    expect(await screen.findByText("Dock 1")).toBeInTheDocument();

    const dateInput = screen.getByLabelText("Date");
    await user.clear(dateInput);
    await user.type(dateInput, "2099-01-01");

    await waitFor(() => {
      expect(fetch).toHaveBeenCalledWith(
        expect.stringContaining("date=2099-01-01"),
      );
    });

    expect(
      await screen.findByText(/No appointments scheduled/i),
    ).toBeInTheDocument();
  });
});
