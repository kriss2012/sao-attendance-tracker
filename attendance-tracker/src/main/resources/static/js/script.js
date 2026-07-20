// SAO Attendance Tracker — client-side "Cardinal Interface" behaviors.
// No external assets; everything here is generated/computed client-side.

document.addEventListener('DOMContentLoaded', () => {
  startSystemClock();
  drawGaugeArcs();
  startCursorGlow();
});

/** Live HH:MM:SS clock shown in the top status bar. */
function startSystemClock() {
  const el = document.querySelector('[data-system-clock]');
  if (!el) return;

  const tick = () => {
    const now = new Date();
    el.textContent = now.toLocaleTimeString('en-GB', { hour12: false });
  };
  tick();
  setInterval(tick, 1000);
}

/**
 * Animates the radial status gauge's SVG arcs from 0 to their target
 * length on page load, using stroke-dasharray/offset math driven by
 * data attributes rendered server-side by Thymeleaf.
 */
function drawGaugeArcs() {
  document.querySelectorAll('.gauge .arc').forEach((circle) => {
    const radius = circle.r.baseVal.value;
    const circumference = 2 * Math.PI * radius;
    const fraction = parseFloat(circle.dataset.fraction || '0');

    circle.style.strokeDasharray = `${circumference}`;
    circle.style.strokeDashoffset = `${circumference}`;

    // Force a reflow so the transition below actually animates.
    circle.getBoundingClientRect();

    const targetOffset = circumference * (1 - fraction);
    requestAnimationFrame(() => {
      circle.style.strokeDashoffset = `${targetOffset}`;
    });
  });
}

/** Subtle cyan glow that follows the pointer, echoing the SAO menu cursor. */
function startCursorGlow() {
  if (window.matchMedia('(prefers-reduced-motion: reduce)').matches) return;
  if (window.matchMedia('(hover: none)').matches) return; // skip on touch devices

  const glow = document.createElement('div');
  glow.style.cssText = `
    position: fixed; width: 220px; height: 220px; border-radius: 50%;
    pointer-events: none; z-index: 9999; top: 0; left: 0;
    background: radial-gradient(circle, rgba(41,242,255,0.08), transparent 70%);
    transform: translate(-50%, -50%); transition: opacity 0.3s ease;
    opacity: 0;
  `;
  document.body.appendChild(glow);

  document.addEventListener('mousemove', (e) => {
    glow.style.opacity = '1';
    glow.style.left = `${e.clientX}px`;
    glow.style.top = `${e.clientY}px`;
  });

  document.addEventListener('mouseleave', () => { glow.style.opacity = '0'; });
}
