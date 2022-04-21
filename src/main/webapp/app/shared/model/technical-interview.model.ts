import dayjs from 'dayjs';
import { IProcess } from 'app/shared/model/process.model';

export interface ITechnicalInterview {
  id?: number;
  date?: string | null;
  score?: number | null;
  technicalStatus?: string | null;
  notes?: string | null;
  editBy?: string | null;
  process?: IProcess | null;
}

export const defaultValue: Readonly<ITechnicalInterview> = {};
