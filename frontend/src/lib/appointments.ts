export const CARRIERS = [
  { id: "carrier-alpha", name: "Alpha Freight" },
  { id: "carrier-beta", name: "Beta Logistics" },
  { id: "carrier-gamma", name: "Gamma Transport" },
] as const;

export type CarrierId = (typeof CARRIERS)[number]["id"];

export const DEFAULT_CARRIER_ID: CarrierId = "carrier-alpha";

export const API_URL =
  process.env.NEXT_PUBLIC_API_URL ?? "http://localhost:8080";

export type DockType = "DRY_VAN" | "REEFER";
export type AppointmentStatus = "SCHEDULED" | "CANCELLED";

export interface Appointment {
  id: number;
  dockId: number;
  dockName: string;
  dockType: DockType;
  carrierId: string;
  carrierName: string;
  trailerType: DockType;
  startTime: string;
  endTime: string;
  status: AppointmentStatus;
}

export function todayIsoDate(): string {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, "0");
  const day = String(now.getDate()).padStart(2, "0");
  return `${year}-${month}-${day}`;
}

export function formatTime(isoDateTime: string): string {
  const date = new Date(isoDateTime);
  return date.toLocaleTimeString([], { hour: "2-digit", minute: "2-digit" });
}

export function dockTypeLabel(dockType: DockType): string {
  return dockType === "DRY_VAN" ? "Dry Van" : "Reefer";
}

export async function fetchAppointments(
  carrierId: string,
  date: string,
): Promise<Appointment[]> {
  const url = `${API_URL}/api/appointments?carrierId=${encodeURIComponent(
    carrierId,
  )}&date=${encodeURIComponent(date)}`;
  const response = await fetch(url);
  if (!response.ok) {
    throw new Error(`Failed to fetch appointments: ${response.status}`);
  }
  return response.json();
}
