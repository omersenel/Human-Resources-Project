import dayjs from 'dayjs';
import { ICandidate } from 'app/shared/model/candidate.model';

export interface IProcess {
  id?: number;
  pdate?: string | null;
  department?: string | null;
  technicalIndicators?: string | null;
  experience?: string | null;
  position?: string | null;
  salaryExpectation?: number | null;
  possibleAssignmnet?: string | null;
  lastStatus?: string | null;
  lastDescription?: string | null;
  editBy?: string | null;
  candidate?: ICandidate | null;
}

export const defaultValue: Readonly<IProcess> = {};
