export interface ICandidate {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  university?: string | null;
  graduationYear?: string | null;
  gpa?: number | null;
  editBy?: string | null;
}

export const defaultValue: Readonly<ICandidate> = {};
