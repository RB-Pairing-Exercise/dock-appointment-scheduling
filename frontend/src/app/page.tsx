"use client";

import { useCallback, useEffect, useState } from "react";
import {
  Appointment,
  CARRIERS,
  CarrierId,
  DEFAULT_CARRIER_ID,
  dockTypeLabel,
  fetchAppointments,
  formatTime,
  todayIsoDate,
} from "@/lib/appointments";

export default function Home() {
  const [carrierId, setCarrierId] = useState<CarrierId>(DEFAULT_CARRIER_ID);
  const [date, setDate] = useState<string>(todayIsoDate());
  const [appointments, setAppointments] = useState<Appointment[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  const loadAppointments = useCallback(async (selectedCarrierId: string, selectedDate: string) => {
    setLoading(true);
    setError(null);
    try {
      const data = await fetchAppointments(selectedCarrierId, selectedDate);
      setAppointments(data);
    } catch {
      setError("Unable to load appointments. Please try again.");
      setAppointments([]);
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    // eslint-disable-next-line react-hooks/set-state-in-effect -- intentional data fetch on carrier/date change
    loadAppointments(carrierId, date);
  }, [carrierId, date, loadAppointments]);

  const activeCarrier = CARRIERS.find((carrier) => carrier.id === carrierId);

  return (
    <div className="min-h-screen bg-zinc-50">
      <header className="border-b border-zinc-200 bg-white">
        <div className="mx-auto flex max-w-4xl flex-wrap items-center justify-between gap-4 px-6 py-4">
          <h1 className="text-xl font-semibold text-zinc-900">
            Carrier Dock Portal
          </h1>
          <label className="flex items-center gap-2 text-sm font-medium text-zinc-700">
            Acting as:
            <select
              aria-label="Acting as carrier"
              value={carrierId}
              onChange={(event) => setCarrierId(event.target.value as CarrierId)}
              className="rounded-md border border-zinc-300 bg-white px-3 py-2 text-sm font-medium text-zinc-900 shadow-sm focus:border-zinc-500 focus:outline-none"
            >
              {CARRIERS.map((carrier) => (
                <option key={carrier.id} value={carrier.id}>
                  {carrier.name}
                </option>
              ))}
            </select>
          </label>
        </div>
      </header>

      <main className="mx-auto max-w-4xl px-6 py-8">
        <div className="mb-6 flex flex-wrap items-end justify-between gap-4">
          <div>
            <label
              htmlFor="appointment-date"
              className="mb-1 block text-sm font-medium text-zinc-700"
            >
              Date
            </label>
            <input
              id="appointment-date"
              type="date"
              value={date}
              onChange={(event) => setDate(event.target.value)}
              className="rounded-md border border-zinc-300 bg-white px-3 py-2 text-sm text-zinc-900 shadow-sm focus:border-zinc-500 focus:outline-none"
            />
          </div>

          <button
            type="button"
            disabled
            title="Coming in the next phase"
            className="cursor-not-allowed rounded-md bg-zinc-300 px-4 py-2 text-sm font-semibold text-zinc-500"
          >
            Request New Appointment
          </button>
        </div>

        <h2 className="mb-4 text-lg font-semibold text-zinc-900">
          {activeCarrier?.name}&rsquo;s Appointments
        </h2>

        {loading && (
          <p className="text-sm text-zinc-500">Loading appointments...</p>
        )}

        {!loading && error && (
          <p className="text-sm text-red-600">{error}</p>
        )}

        {!loading && !error && appointments.length === 0 && (
          <div className="rounded-lg border border-dashed border-zinc-300 bg-white p-8 text-center text-sm text-zinc-500">
            No appointments scheduled for {activeCarrier?.name} on this date.
          </div>
        )}

        {!loading && !error && appointments.length > 0 && (
          <ul className="flex flex-col gap-3">
            {appointments.map((appointment) => (
              <li
                key={appointment.id}
                className="flex flex-wrap items-center justify-between gap-3 rounded-lg border border-zinc-200 bg-white p-4 shadow-sm"
              >
                <div>
                  <p className="font-semibold text-zinc-900">
                    {appointment.dockName}
                  </p>
                  <p className="text-sm text-zinc-500">
                    {formatTime(appointment.startTime)} &ndash;{" "}
                    {formatTime(appointment.endTime)}
                  </p>
                </div>
                <div className="flex items-center gap-2">
                  <span className="rounded-full bg-zinc-100 px-3 py-1 text-xs font-medium text-zinc-700">
                    {dockTypeLabel(appointment.dockType)}
                  </span>
                  <span className="rounded-full bg-emerald-100 px-3 py-1 text-xs font-medium text-emerald-700">
                    {appointment.status}
                  </span>
                </div>
              </li>
            ))}
          </ul>
        )}
      </main>
    </div>
  );
}
