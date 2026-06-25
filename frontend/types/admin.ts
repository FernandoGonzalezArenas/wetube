export type ReportType= 'VIDEO' | 'USER';

export const PREDEFINED_REASONS = [
{value: 'SPAM', label: 'Spam o contenido engañoso'},
{value: 'VIOLENCIA', label: 'Contenido violento o explicito'},
{value: 'ACOSO', label: 'Acoso o civerbullying'},
{value: 'DERECHOS_AUTOR', label: 'Infraccion por derechos de autor'},
{value: 'OTROS', label: 'Otros motivos'}
]

export interface auditorDto {
    reason: string;
}

export interface reportCreateDto {
    reason: string;
    description: string;
}

export interface IndividualReport {
    reportId: number;
    description: string;
    createdAt: string;
}

export interface ReportDetail {
    targetId: number;
    type: ReportType;
    status: string;
    totalReports: number;
    reasonsCount: Record<string, number>;
    reportDescriptions: IndividualReport[];
    videoUserId?: number;
    videoTitle?: string;
    videoDescription?: string;
    videoDuration?: number;
    videoUrl?: string;
    thumbnailUrl?: string;
    videoCreatedAt?: string;
    targetUsername?: string;
    profilePictureUrl?: string;
}
