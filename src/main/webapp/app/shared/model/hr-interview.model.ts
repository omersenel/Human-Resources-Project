import dayjs from 'dayjs';
import { IProcess } from 'app/shared/model/process.model';

export interface IHrInterview {
  id?: number;
  date?: string | null;
  score?: number | null;
  ikStatus?: string | null;
  notes?: string | null;
  editBy?: string | null;
  process?: IProcess | null;
}

export const defaultValue: Readonly<IHrInterview> = {};
