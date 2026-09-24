-- Phase 5.4: the hand-adjusted shape of a right-angle edge, as JSON (EdgeRoute): the number
-- of segments it was adjusted for and each draggable segment's offset from the default
-- route. Null means the default route.
alter table prerequisites add column route text;
